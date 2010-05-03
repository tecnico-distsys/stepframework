@ECHO OFF
CALL groovy WorkloadGenerator.groovy -s 889900 -n 10     -o build\load-10.obj     -p db.properties --names data\names.txt --surnames data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 312423 -n 100    -o build\load-100.obj    -p db.properties --names data\names.txt --surnames data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 925447 -n 1000   -o build\load-1000.obj   -p db.properties --names data\names.txt --surnames data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 245374 -n 10000  -o build\load-10000.obj  -p db.properties --names data\names.txt --surnames data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 523429 -n 100000 -o build\load-100000.obj -p db.properties --names data\names.txt --surnames data\surnames.txt
