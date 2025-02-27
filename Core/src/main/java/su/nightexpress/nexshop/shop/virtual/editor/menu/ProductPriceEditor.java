package su.nightexpress.nexshop.shop.virtual.editor.menu;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorLocale;
import su.nexmedia.engine.api.menu.impl.EditorMenu;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.*;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.shop.util.TimeUtils;
import su.nightexpress.nexshop.api.shop.ProductPricer;
import su.nightexpress.nexshop.api.type.PriceType;
import su.nightexpress.nexshop.api.type.TradeType;
import su.nightexpress.nexshop.config.Lang;
import su.nightexpress.nexshop.data.price.ProductPriceStorage;
import su.nightexpress.nexshop.shop.price.DynamicProductPricer;
import su.nightexpress.nexshop.shop.price.FloatProductPricer;
import su.nightexpress.nexshop.shop.price.RangedProductPricer;
import su.nightexpress.nexshop.shop.virtual.editor.VirtualLocales;
import su.nightexpress.nexshop.shop.virtual.impl.product.VirtualProduct;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ProductPriceEditor extends EditorMenu<ExcellentShop, VirtualProduct> {

    private static final String TEXTURE_PRICE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmI5Mjk5YjcyNGM1ZDM0YWM5M2VkZTc1NjAxZGZlYjBiZGE1NzhkNzBiOGY0ZDdmODJkNzY3NmYwYzZjMTE0YSJ9fX0=";
    private static final String TEXTURE_BUY = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY2Yjg1ZjYyNjQ0NGRiZDViZGRmN2E1MjFmZTUyNzQ4ZmU0MzU2NGUwM2ZiZDM1YjZiNWU3OTdkZTk0MmQifX19";
    private static final String TEXTURE_SELL = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTlkYmVkNTIyZThkZTFhNjgxZGRkZDM3ODU0ZWU0MjY3ZWZjNDhiNTk5MTdmOWE5YWNiNDIwZDZmZGI5In19fQ==";
    private static final String TEXTURE_REFRESH = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDJkOTkzNDJiZTY3MTU2Y2VmNDhiNTBhOTcxOTI5ODkyMjJkNTY5YjQ0NjA5Mjg5OWNjMjMzNDc1NDJlNzFhYSJ9fX0=";
    private static final String TEXTURE_INITIAL = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODI0Zjc2OWM5NDUwZjIyZTQ4NGUwODljYTAyZTMyNGZlMzdiMThmNGMxOGVmMjk2MDIxODcxYmE0YWQwYzM5NiJ9fX0=";
    private static final String TEXTURE_STEP = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2U0ZjJmOTY5OGMzZjE4NmZlNDRjYzYzZDJmM2M0ZjlhMjQxMjIzYWNmMDU4MTc3NWQ5Y2VjZDcwNzUifX19";


    public ProductPriceEditor(@NotNull VirtualProduct product) {
        super(product.getShop().plugin(), product, Placeholders.EDITOR_VIRTUAL_TITLE, 27);

        this.addReturn(22).setClick((viewer, event) ->
            this.object.getEditor().open(viewer.getPlayer(), 1)
        );

        this.addItem(ItemUtil.createCustomHead(TEXTURE_PRICE), VirtualLocales.PRODUCT_PRICE_TYPE, 4).setClick((viewer, event) -> {
            PriceType priceType = CollectionsUtil.next(product.getPricer().getType());
            product.setPricer(ProductPricer.from(priceType));
            ProductPriceStorage.deleteData(product);
            this.save(viewer);
        });

        this.addItem(ItemUtil.createCustomHead(TEXTURE_BUY), VirtualLocales.PRODUCT_PRICE_FLAT_BUY, 10).setClick((viewer, event) -> {
            ProductPricer pricer = product.getPricer();
            if (event.getClick() == ClickType.DROP) {
                if (pricer instanceof RangedProductPricer ranged) {
                    ranged.setPriceMin(TradeType.BUY, -1);
                    ranged.setPriceMax(TradeType.BUY, -1);
                } else pricer.setPrice(TradeType.BUY, -1);
                this.save(viewer);
            } else {
                this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_PRODUCT_ENTER_PRICE), chat -> {
                    double price = StringUtil.getDouble(Colorizer.strip(chat.getMessage()), -1D, true);
                    if (pricer instanceof RangedProductPricer ranged) {
                        if (event.isLeftClick()) {
                            ranged.setPriceMin(TradeType.BUY, price);
                        } else ranged.setPriceMax(TradeType.BUY, price);
                    } else pricer.setPrice(TradeType.BUY, price);

                    product.getShop().saveProducts();
                    return true;
                });
            }

        }).getOptions().setDisplayModifier((viewer, item) -> {
            ProductPricer pricer = product.getPricer();
            EditorLocale locale = switch (pricer.getType()) {
                case FLAT -> VirtualLocales.PRODUCT_PRICE_FLAT_BUY;
                case FLOAT -> VirtualLocales.PRODUCT_PRICE_FLOAT_BUY;
                case DYNAMIC -> VirtualLocales.PRODUCT_PRICE_DYNAMIC_BUY;
            };
            item.editMeta(meta -> {
                meta.displayName(ComponentUtil.asComponent(locale.getLocalizedName()));
                meta.lore(ComponentUtil.asComponent(locale.getLocalizedLore()));
                meta.addItemFlags(ItemFlag.values());
                ItemUtil.replaceNameAndLore(meta, product.replacePlaceholders());
            });
        });

        this.addItem(ItemUtil.createCustomHead(TEXTURE_SELL), VirtualLocales.PRODUCT_PRICE_FLAT_SELL, 11).setClick((viewer, event) -> {
            ProductPricer pricer = product.getPricer();
            if (event.getClick() == ClickType.DROP) {
                if (pricer instanceof RangedProductPricer ranged) {
                    ranged.setPriceMin(TradeType.SELL, -1);
                    ranged.setPriceMax(TradeType.SELL, -1);
                } else pricer.setPrice(TradeType.SELL, -1);
                this.save(viewer);
            } else {
                this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_PRODUCT_ENTER_PRICE), chat -> {
                    double price = StringUtil.getDouble(Colorizer.strip(chat.getMessage()), -1D, true);
                    if (pricer instanceof RangedProductPricer ranged) {
                        if (event.isLeftClick()) {
                            ranged.setPriceMin(TradeType.SELL, price);
                        } else ranged.setPriceMax(TradeType.SELL, price);
                    } else pricer.setPrice(TradeType.SELL, price);

                    product.getShop().saveProducts();
                    return true;
                });
            }

        }).getOptions().setDisplayModifier((viewer, item) -> {
            ProductPricer pricer = product.getPricer();
            EditorLocale locale = switch (pricer.getType()) {
                case FLAT -> VirtualLocales.PRODUCT_PRICE_FLAT_SELL;
                case FLOAT -> VirtualLocales.PRODUCT_PRICE_FLOAT_SELL;
                case DYNAMIC -> VirtualLocales.PRODUCT_PRICE_DYNAMIC_SELL;
            };
            item.editMeta(meta -> {
                meta.displayName(ComponentUtil.asComponent(locale.getLocalizedName()));
                meta.lore(ComponentUtil.asComponent(locale.getLocalizedLore()));
                meta.addItemFlags(ItemFlag.values());
                ItemUtil.replaceNameAndLore(meta, product.replacePlaceholders());
            });
        });

        this.addItem(ItemUtil.createCustomHead(TEXTURE_REFRESH), VirtualLocales.PRODUCT_PRICE_FLOAT_REFRESH, 15).setClick((viewer, event) -> {
            FloatProductPricer pricer = (FloatProductPricer) product.getPricer();
            if (event.isShiftClick()) {
                if (event.isLeftClick()) {
                    pricer.getDays().clear();
                } else pricer.getTimes().clear();
                this.save(viewer);
                return;
            }

            if (event.isLeftClick()) {
                EditorManager.suggestValues(viewer.getPlayer(), CollectionsUtil.getEnumsList(DayOfWeek.class), true);
                this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_GENERIC_ENTER_DAY), chat -> {
                    DayOfWeek day = StringUtil.getEnum(chat.getMessage(), DayOfWeek.class).orElse(null);
                    if (day == null) return true;

                    pricer.getDays().add(day);
                    //pricer.stopScheduler();
                    //pricer.startScheduler();
                    product.getShop().saveProducts();
                    return true;
                });
            } else {
                this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_GENERIC_ENTER_TIME), chat -> {
                    try {
                        pricer.getTimes().add(LocalTime.parse(chat.getMessage(), TimeUtils.TIME_FORMATTER));
                        //pricer.stopScheduler();
                        //pricer.startScheduler();
                        product.getShop().saveProducts();
                    } catch (DateTimeParseException ignored) {}
                    return true;
                });
            }
        }).getOptions().setVisibilityPolicy(viewer -> product.getPricer().getType() == PriceType.FLOAT);

        this.addItem(ItemUtil.createCustomHead(TEXTURE_INITIAL), VirtualLocales.PRODUCT_PRICE_DYNAMIC_INITIAL, 15).setClick((viewer, event) -> {
            this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_PRODUCT_ENTER_PRICE), chat -> {
                DynamicProductPricer pricer = (DynamicProductPricer) product.getPricer();
                double price = StringUtil.getDouble(chat.getMessage(), 0D);
                if (event.isLeftClick()) {
                    pricer.setInitial(TradeType.BUY, price);
                } else pricer.setInitial(TradeType.SELL, price);

                product.getShop().saveProducts();
                return true;
            });
        }).getOptions().setVisibilityPolicy(viewer -> product.getPricer().getType() == PriceType.DYNAMIC);

        this.addItem(ItemUtil.createCustomHead(TEXTURE_STEP), VirtualLocales.PRODUCT_PRICE_DYNAMIC_STEP, 16).setClick((viewer, event) -> {
            this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_PRODUCT_ENTER_PRICE), chat -> {
                DynamicProductPricer pricer = (DynamicProductPricer) product.getPricer();
                double price = StringUtil.getDouble(chat.getMessage(), 0D);
                if (event.isLeftClick()) {
                    pricer.setStep(TradeType.BUY, price);
                } else pricer.setStep(TradeType.SELL, price);

                product.getShop().saveProducts();
                return true;
            });
        }).getOptions().setVisibilityPolicy(viewer -> product.getPricer().getType() == PriceType.DYNAMIC);

        this.getItems().forEach(menuItem -> {
            if (menuItem.getOptions().getDisplayModifier() == null) {
                menuItem.getOptions().setDisplayModifier((viewer, item) -> ItemUtil.replaceNameAndLore(item, product.replacePlaceholders()));
            }
        });
    }

    private void save(@NotNull MenuViewer viewer) {
        this.object.getShop().saveProducts();
        this.plugin.runTask(task -> this.open(viewer.getPlayer(), viewer.getPage()));
    }
}
