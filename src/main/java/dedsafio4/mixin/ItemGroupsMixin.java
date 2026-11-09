package dedsafio4.mixin;

import dedsafio4.Dedsafio4;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/** Esconde las pestañas viejas de los mods para que solo quede la pestaña "Dedsafio". */
@Mixin(ItemGroups.class)
public class ItemGroupsMixin {
	@Inject(method = "getGroupsToDisplay", at = @At("RETURN"), cancellable = true, require = 0)
	private static void dedsafio4$ocultarViejas(CallbackInfoReturnable<List<ItemGroup>> cir) {
		List<ItemGroup> resultado = new ArrayList<>();
		for (ItemGroup grupo : cir.getReturnValue()) {
			Identifier id = Registries.ITEM_GROUP.getId(grupo);
			if (id != null && Dedsafio4.PESTANAS_OCULTAS.contains(id.getNamespace())) continue;
			resultado.add(grupo);
		}
		cir.setReturnValue(resultado);
	}
}
