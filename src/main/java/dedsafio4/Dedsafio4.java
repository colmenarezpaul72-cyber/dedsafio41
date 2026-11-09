package dedsafio4;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dedsafio4 implements ModInitializer {
	public static final String ID = "dedsafio4";

	/** Mods cuyos items entran al catalogo unico "Dedsafio". El orden es la prioridad cuando un item esta repetido. */
	public static final List<String> NAMESPACES = List.of(
			ID, "ruleta", "ruleta_anim", "ghostitem", "ghostsafio", "sacos", "silla");

	/** Pestañas viejas que se esconden (reemplazadas por "Dedsafio"). */
	public static final Set<String> PESTANAS_OCULTAS = Set.of(
			"ruleta", "ruleta_anim", "ghostitem", "ghostsafio", "sacos", "silla");

	/** Items que NO aparecen en el catalogo (totems y la nave duplicada). */
	private static boolean excluido(Identifier id) {
		String p = id.getPath();
		if (p.contains("totem")) return true;
		return id.getNamespace().equals("ruleta") && p.equals("nave_espacial");
	}

	public static Item ANTIDOTO, FRUTA_ESPACIO, FRUTO_QUIU, GIGANTOFRUTA, PASTILLA_VACIA,
			SANGRE_REPTISAURIO, CARNE_GLEBANOIDE, CEREBRO_GLEBA, COSTILLA, CUERO, ESPADA_GLEBA;

	private static Item reg(String id, Item item) {
		return Registry.register(Registries.ITEM, Identifier.of(ID, id), item);
	}

	private static Item.Settings s() {
		return new Item.Settings();
	}

	@Override
	public void onInitialize() {
		ANTIDOTO = reg("antidoto", new AntidotoItem(s().maxCount(16).food(
				new FoodComponent.Builder().nutrition(0).saturationModifier(0f).alwaysEdible().build())));
		FRUTA_ESPACIO = reg("fruta_espacio", new Item(s().food(
				new FoodComponent.Builder().nutrition(4).saturationModifier(0.4f).alwaysEdible().build())));
		FRUTO_QUIU = reg("fruto_quiu", new Item(s().food(
				new FoodComponent.Builder().nutrition(3).saturationModifier(0.3f).build())));
		GIGANTOFRUTA = reg("gigantofruta", new Item(s().food(
				new FoodComponent.Builder().nutrition(8).saturationModifier(0.8f).build())));
		CARNE_GLEBANOIDE = reg("carne_glebanoide", new Item(s().food(
				new FoodComponent.Builder().nutrition(5).saturationModifier(0.5f).meat()
						.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 200), 0.3f).build())));
		PASTILLA_VACIA = reg("pastilla_vacia", new Item(s()));
		SANGRE_REPTISAURIO = reg("sangre_reptisaurio", new Item(s()));
		CEREBRO_GLEBA = reg("cerebro_gleba", new Item(s()));
		COSTILLA = reg("costilla", new Item(s()));
		CUERO = reg("cuero", new Item(s()));
		ESPADA_GLEBA = reg("espada_gleba", new SwordItem(ToolMaterials.IRON, s().attributeModifiers(
				SwordItem.createAttributeModifiers(ToolMaterials.IRON, 3, -2.4f))));

		registrarPestana();
		mejorarGleba();
	}

	private static void registrarPestana() {
		RegistryKey<ItemGroup> clave = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(ID, "dedsafio"));
		Registry.register(Registries.ITEM_GROUP, clave, FabricItemGroup.builder()
				.icon(() -> {
					Item icono = Registries.ITEM.get(Identifier.of("ghostsafio", "manzana_de_ambar"));
					return new ItemStack(icono == Items.AIR ? GIGANTOFRUTA : icono);
				})
				.displayName(Text.translatable("itemGroup.dedsafio4.dedsafio"))
				.entries((contexto, entradas) -> {
					List<Item> normales = new ArrayList<>();
					List<Item> bloques = new ArrayList<>();
					Set<String> vistos = new HashSet<>();
					for (String ns : NAMESPACES) {
						List<Identifier> ids = new ArrayList<>();
						for (Identifier id : Registries.ITEM.getIds()) {
							if (id.getNamespace().equals(ns)) ids.add(id);
						}
						ids.sort(null);
						for (Identifier id : ids) {
							if (excluido(id)) continue;
							if (!vistos.add(id.getPath())) continue; // repetido en otro mod: se queda el de mayor prioridad
							Item item = Registries.ITEM.get(id);
							if (item == Items.AIR) continue;
							(item instanceof BlockItem ? bloques : normales).add(item);
						}
					}
					for (Item item : normales) entradas.add(item);
					for (Item item : bloques) entradas.add(item); // los bloques van de ultimo
				})
				.build());
	}

	/** Agrega a Gleba los features que ya existian en los datos de Ghostsafio pero no estaban conectados al bioma. */
	private static void mejorarGleba() {
		RegistryKey<Biome> gleba = RegistryKey.of(RegistryKeys.BIOME, Identifier.of("ghostsafio", "gleba"));
		agregar(gleba, GenerationStep.Feature.UNDERGROUND_ORES, "grasa_ore");
		agregar(gleba, GenerationStep.Feature.VEGETAL_DECORATION, "raices_colgantes");
		agregar(gleba, GenerationStep.Feature.VEGETAL_DECORATION, "cuerdas_colgantes");
	}

	private static void agregar(RegistryKey<Biome> bioma, GenerationStep.Feature paso, String feature) {
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(bioma), paso,
				RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of("ghostsafio", feature)));
	}
}
