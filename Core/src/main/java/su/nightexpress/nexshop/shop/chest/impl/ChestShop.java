package su.nightexpress.nexshop.shop.chest.impl;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.ICleanable;
import su.nexmedia.engine.config.EngineConfig;
import su.nexmedia.engine.lang.LangManager;
import su.nexmedia.engine.utils.LocationUtil;
import su.nexmedia.engine.utils.NumberUtil;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.api.currency.ICurrency;
import su.nightexpress.nexshop.api.shop.Shop;
import su.nightexpress.nexshop.api.shop.ShopView;
import su.nightexpress.nexshop.api.type.TradeType;
import su.nightexpress.nexshop.shop.FlatProductPricer;
import su.nightexpress.nexshop.shop.chest.ChestDisplayHandler;
import su.nightexpress.nexshop.shop.chest.ChestShopModule;
import su.nightexpress.nexshop.shop.chest.config.ChestConfig;
import su.nightexpress.nexshop.shop.chest.config.ChestLang;
import su.nightexpress.nexshop.shop.chest.editor.menu.ShopEditor;
import su.nightexpress.nexshop.shop.chest.type.ChestShopType;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChestShop extends Shop<ChestShop, ChestProduct> implements ICleanable {

    private final ChestShopModule module;

    private Location        location;
    private UUID            ownerId;
    private String          ownerName;
    private OfflinePlayer ownerPlayer;
    private ChestShopType type;

    private ShopEditor editor;

    private List<String> displayText;
    private Location     displayHologramLoc;
    private Location     displayItemLoc;

    private ShopView<ChestShop> view;

    public ChestShop(@NotNull ChestShopModule module, @NotNull Player owner, @NotNull Container container, @NotNull ChestShopType type) {
        this(module, new JYML(module.getFullPath() + ChestShopModule.DIR_SHOPS, UUID.randomUUID() + ".yml"));
        this.setBank(new ChestShopBank(this));
        this.setLocation(container.getLocation());
        this.setType(type);
        this.setOwner(owner);
        this.setName(Placeholders.Player.replacer(owner).apply(ChestConfig.DEFAULT_NAME.get()));
        Stream.of(TradeType.values()).forEach(tradeType -> this.setTransactionEnabled(tradeType, true));
    }

    public ChestShop(@NotNull ChestShopModule module, @NotNull JYML cfg) {
        super(module.plugin(), cfg);
        this.module = module;
    }

    @Override
    public boolean load() {
        this.setBank(new ChestShopBank(this));

        Location location = cfg.getLocation("Location");
        if (location == null || !ChestShopModule.isValidContainer(location.getBlock())) {
            this.plugin.error("Shop block is not a valid container!");
            return false;
        }
        this.setLocation(location);

        this.setType(cfg.getEnum("Type", ChestShopType.class, ChestShopType.PLAYER));
        try {
            this.ownerId = UUID.fromString(cfg.getString("Owner.Id", ""));
            this.ownerPlayer = plugin.getServer().getOfflinePlayer(this.getOwnerId());
            if (this.ownerPlayer.getName() == null) {
                throw new IllegalArgumentException("Invalid owner!");
            }
        }
        catch (IllegalArgumentException e) {
            this.plugin.error("Shop owner is invalid!");
            return false;
        }

        this.ownerName = this.isAdminShop() ? ChestConfig.ADMIN_SHOP_NAME : this.ownerPlayer.getName();
        this.setName(cfg.getString("Name", this.getOwnerName()));

        for (TradeType tradeType : TradeType.values()) {
            this.setTransactionEnabled(tradeType, cfg.getBoolean("Transaction_Allowed." + tradeType.name(), true));
        }
        for (ICurrency currency : ChestConfig.ALLOWED_CURRENCIES) {
            this.getBank().deposit(currency, cfg.getDouble("Bank." + currency.getId()));
        }

        this.loadProducts();
        this.setupView();
        return true;
    }

    private void loadProducts() {
        this.cfg.getSection("Products").stream().map(id -> {
            try {
                return ChestProduct.read(cfg, "Products." + id, id);
            }
            catch (Exception e) {
                this.plugin.error("Could not load '" + id + "' product in '" + getId() + "' shop!");
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).forEach(this::addProduct);
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        Location location = this.getLocation();
        World world = this.getContainer().getWorld();

        return str -> super.replacePlaceholders().apply(str
            .replace(Placeholders.SHOP_BANK_BALANCE, ChestConfig.ALLOWED_CURRENCIES.stream()
                                                                                   .map(currency -> currency.format(this.getBank().getBalance(currency)))
                                                                                   .collect(Collectors.joining(DELIMITER_DEFAULT)))
            .replace(Placeholders.SHOP_CHEST_OWNER, this.getOwnerName())
            .replace(Placeholders.SHOP_CHEST_LOCATION_X, NumberUtil.format(location.getX()))
            .replace(Placeholders.SHOP_CHEST_LOCATION_Y, NumberUtil.format(location.getY()))
            .replace(Placeholders.SHOP_CHEST_LOCATION_Z, NumberUtil.format(location.getZ()))
            .replace(Placeholders.SHOP_CHEST_LOCATION_WORLD, EngineConfig.getWorldName(world.getName()))
            .replace(Placeholders.SHOP_CHEST_IS_ADMIN, LangManager.getBoolean(this.isAdminShop()))
            .replace(Placeholders.SHOP_CHEST_TYPE, plugin.getLangManager().getEnum(this.getType()))
        );
    }

    @Override
    public void clear() {
        ChestDisplayHandler displayHandler = this.module.getDisplayHandler();
        if (displayHandler != null) {
            displayHandler.remove(this);
        }

        if (this.editor != null) {
            this.editor.clear();
            this.editor = null;
        }
        this.products.values().forEach(ChestProduct::clear);
        this.products.clear();
    }

    @Override
    public void onSave() {
        cfg.set("Location", this.getLocation());
        cfg.set("Name", this.getName());
        cfg.set("Owner.Id", this.getOwnerId().toString());
        cfg.set("Type", this.getType().name());
        this.transactions.forEach((type, isAllowed) -> {
            cfg.set("Transaction_Allowed." + type.name(), isAllowed);
        });
        this.getBank().getBalance().forEach((currencyId, balance) -> {
            cfg.set("Bank." + currencyId, balance);
        });
        this.cfg.set("Products", null);
        this.getProducts().forEach(product -> product.write(cfg, "Products." + product.getId()));
    }

    @Override
    @NotNull
    protected ChestShop get() {
        return this;
    }

    @NotNull
    public ChestShopModule getModule() {
        return module;
    }

    @Override
    public void setupView() {
        this.view = new ChestShopView(this);
    }

    @Override
    @NotNull
    public ShopView<ChestShop> getView() {
        return this.view;
    }

    @Override
    @NotNull
    public ShopEditor getEditor() {
        if (this.editor == null) {
            this.editor = new ShopEditor(this);
        }
        return this.editor;
    }

    @Override
    public boolean canAccess(@NotNull Player player, boolean notify) {
        return true;
    }

    public boolean createProduct(@NotNull Player player, @NotNull ItemStack item) {
        if (item.getType().isAir() || this.isProduct(item)) {
            return false;
        }
        if (!ChestConfig.isAllowedItem(item)) {
            plugin.getMessage(ChestLang.SHOP_PRODUCT_ERROR_BAD_ITEM).send(player);
            return false;
        }

        ChestProduct shopProduct = new ChestProduct(ChestConfig.DEFAULT_CURRENCY, item);
        shopProduct.setStock(new ChestProductStock());
        shopProduct.setPricer(new FlatProductPricer());
        this.addProduct(shopProduct);
        return true;
    }

    @NotNull
    public ChestShopType getType() {
        return type;
    }

    public void setType(@NotNull ChestShopType type) {
        this.type = type;
    }

    public boolean isAdminShop() {
        return this.getType() == ChestShopType.ADMIN;
    }

    @NotNull
    public Container getContainer() {
        return (Container) this.getLocation().getBlock().getState();
    }

    public boolean isDoubleChest() {
        return this.getContainer().getInventory() instanceof DoubleChestInventory;
    }

    @NotNull
    public Set<Chest> getChestSides() {
        if (!(this.getContainer() instanceof Chest chest)) return Collections.emptySet();

        Set<Chest> chests = new HashSet<>();
        chests.add(chest);

        if (!this.isDoubleChest()) return chests;

        DoubleChest doubleChest = (DoubleChest) this.getInventory().getHolder();
        if (doubleChest == null) return chests;

        Chest left = (Chest) doubleChest.getLeftSide();
        Chest right = (Chest) doubleChest.getRightSide();
        if (left != null) chests.add(left);
        if (right != null) chests.add(right);

        return chests;
    }

    @NotNull
    public Inventory getInventory() {
        if (this.isDoubleChest()) {
            DoubleChest doubleChest = (DoubleChest) this.getContainer().getInventory().getHolder();
            if (doubleChest != null) {
                return doubleChest.getInventory();
            }
        }
        return this.getContainer().getInventory();
    }

    public void teleport(@NotNull Player player) {
        Location location = this.getLocation().clone();
        Block block = location.getBlock();
        BlockData data = block.getBlockData();
        if (data instanceof Directional directional) {
            Block opposite = block.getRelative(directional.getFacing()).getLocation().clone().add(0, 0.5, 0).getBlock();
            location = LocationUtil.getCenter(opposite.getLocation());
            location.setDirection(directional.getFacing().getOppositeFace().getDirection());
            location.setPitch(35F);
        }
        player.teleport(location);
    }

    @NotNull
    public Location getLocation() {
        return this.location;
    }

    private void setLocation(@NotNull Location location) {
        this.location = location.clone();
    }

    @NotNull
    public UUID getOwnerId() {
        return this.ownerId;
    }

    public void setOwner(@NotNull OfflinePlayer player) {
        this.ownerId = player.getUniqueId();
        this.ownerName = this.isAdminShop() ? ChestConfig.ADMIN_SHOP_NAME : player.getName();
        this.ownerPlayer = player;
    }

    @NotNull
    public String getOwnerName() {
        return this.ownerName;
    }

    @NotNull
    public OfflinePlayer getOwner() {
        return this.ownerPlayer;
    }

    public boolean isOwner(@NotNull Player player) {
        return this.getOwnerId().equals(player.getUniqueId());
    }

    /*@Override
    public boolean isDisplayCreated() {
        return this.displayCreated;
    }

    @Override
    public void setDisplayCreated(boolean displayCreated) {
        this.displayCreated = displayCreated;
    }*/

    @NotNull
    public List<String> getDisplayText() {
        if (this.displayText == null) this.updateDisplayText();
        return this.displayText;
    }

    public void updateDisplayText() {
        this.displayText = new ArrayList<>(ChestConfig.getDisplayText(this.getType()));
        this.displayText.replaceAll(this.replacePlaceholders());
    }

    public void updateDisplay() {
        this.displayHologramLoc = null;
        this.displayItemLoc = null;

        this.updateDisplayText();

        ChestDisplayHandler displayHandler = this.module.getDisplayHandler();
        if (displayHandler != null) {
            displayHandler.remove(this);
            displayHandler.create(this);
        }
    }

    private double getDisplayYOffset() {
        if (this.getContainer() instanceof Chest) {
            return -1D;
        }
        else return -0.85D;
    }

    @NotNull
    public Location getDisplayLocation() {
        if (this.displayHologramLoc == null) {
            Location invLocation = this.getInventory().getLocation();
            if (invLocation == null || !this.isDoubleChest()) {
                Location center = LocationUtil.getCenter(this.getLocation().clone());
                this.displayHologramLoc = center.add(0, this.getDisplayYOffset(), 0);
            }
            else {
                this.displayHologramLoc = invLocation.add(0.5, -0.5, 0.5);
            }
        }
        return this.displayHologramLoc;
    }

    @NotNull
    public Location getDisplayItemLocation() {
        if (this.displayItemLoc == null) {
            Location glassLocation = this.getDisplayLocation();
            this.displayItemLoc = glassLocation.clone().add(0, 1.4, 0);
        }
        return this.displayItemLoc;
    }
}
