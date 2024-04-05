package dev.latvian.mods.projectex.menu;

import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.block.entity.AlchemyTableEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, ProjectEX.MOD_ID);

    public static final RegistryObject<MenuType<AlchemyTableMenu>> ALCHEMY_TABLE = register("alchemy_table", AlchemyTableMenu::new);

    public static final RegistryObject<MenuType<ArcaneTabletMenu>> ARCANE_TABLET = register("arcane_tablet", ArcaneTabletMenu::new);
    public static final RegistryObject<MenuType<StoneTableMenu>> STONE_TABLE = register("stone_table", StoneTableMenu::new);


    private static <C extends AbstractContainerMenu, T extends MenuType<C>> RegistryObject<T> register(String name, IContainerFactory<? extends C> f) {
        //noinspection unchecked
        return REGISTRY.register(name, () -> (T) IForgeContainerType.create(f));
    }
}
