#!/usr/bin/env python3
"""Mete los .jar de la carpeta 'originales' DENTRO del jar de Dedsafio 4 (jar-in-jar de Fabric).
Uso: unir_jars.py <dedsafio4.jar> <carpeta_originales> <salida.jar>"""
import sys, os, json, glob, zipfile
src, carpeta, salida = sys.argv[1:4]
jars = sorted(glob.glob(os.path.join(carpeta, '*.jar')))
with zipfile.ZipFile(src) as zi, zipfile.ZipFile(salida, 'w', zipfile.ZIP_DEFLATED) as zo:
    for it in zi.infolist():
        data = zi.read(it.filename)
        if it.filename == 'fabric.mod.json':
            meta = json.loads(data.decode('utf-8'))
            meta['jars'] = [{'file': 'META-INF/jars/' + os.path.basename(j)} for j in jars]
            data = json.dumps(meta, indent=2, ensure_ascii=False).encode('utf-8')
        if it.is_dir():
            continue
        zo.writestr(it.filename, data)
    for j in jars:
        zo.write(j, 'META-INF/jars/' + os.path.basename(j))
print('Listo:', salida, '->', len(jars), 'mods dentro')
