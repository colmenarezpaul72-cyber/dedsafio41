# Dedsafio 4 – mod unificado (Fabric 1.21.1)

## Cómo convertirlo en .jar con GitHub
1. Crea un repositorio nuevo en GitHub y sube TODO el contenido de esta carpeta (incluida `.github`).
2. Entra a la pestaña **Actions** → "Compilar Dedsafio 4" (se ejecuta solo al subir; también puedes darle *Run workflow*).
3. Cuando termine en verde, abre la ejecución y baja el archivo de **Artifacts** → `dedsafio4`.
4. Si falla en rojo, copia el mensaje de error y me lo pegas.

## Qué hace
- Una sola pestaña creativa **Dedsafio** con los ítems de Ruleta, Ghostsafio, Ghostitem, Sacos y Silla; **los bloques van al final**.
- Las pestañas viejas se esconden. Los **tótems** y la `nave_espacial` de Ruleta no salen en el catálogo (siguen existiendo con `/give`).
- Ítems repetidos entre mods (corazones, ámbar, grasa…): se muestra uno solo (prioridad Ruleta > Ghostitem > Ghostsafio).
- 11 ítems nuevos con tus texturas: Antídoto, Fruta del Espacio, Fruto de Quiu, Gigantofruta, Pastilla Vacía, Sangre de Reptisaurio, Carne Glebanoide, Cerebro de Gleba, Costilla, Cuero y Espada de Gleba.
- Gleba: se conectan al bioma `grasa_ore`, `raices_colgantes` y `cuerdas_colgantes` (ya existían en los datos de Ghostsafio pero no estaban activos).
- Descripciones: en `src/main/resources/assets/dedsafio4/lang/es_es.json` agrega líneas `"item.<mod>.<item>.desc": "texto"` (por ejemplo `"item.ruleta.ambar.desc"`). Salen en gris en el tooltip. Para varias líneas usa `\n`.

## Los otros mods
Este mod NO trae adentro los 5 mods originales; instálalos junto a él en la carpeta `mods`.
Para tener **un solo archivo**: sube los 5 `.jar` originales a la carpeta `originales/` del repositorio
(los grandes, 39 MB y 69 MB, necesitan `git` o GitHub Desktop; la web solo deja 25 MB por archivo).
El workflow genera entonces `dedsafio4-COMPLETO.jar` con todo adentro.

## Pendiente (necesita más trabajo)
- Aspecto de Gleba (suelo blanco, árboles rojos enormes, cielo): vive en el código compilado de Ghostsafio.
- Unificar el cohete "Nave": solo está oculto el de Ruleta.
- Textos de descripción oficiales de Dedsafio 4.
