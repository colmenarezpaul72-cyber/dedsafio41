package dedsafio4.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

/**
 * Descripciones de items: para cualquier item de cualquier mod basta agregar en
 * assets/dedsafio4/lang/es_es.json una linea  "item.<mod>.<item>.desc": "texto".
 */
public class Dedsafio4Client implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ItemTooltipCallback.EVENT.register((stack, contexto, tipo, lineas) -> {
			Identifier id = Registries.ITEM.getId(stack.getItem());
			String clave = "item." + id.getNamespace() + "." + id.getPath() + ".desc";
			if (I18n.hasTranslation(clave)) {
				for (String linea : I18n.translate(clave).split("\\n")) {
					if (!linea.isBlank()) lineas.add(Text.literal(linea).formatted(Formatting.GRAY));
				}
			}
		});
	}
}
