@ECHO OFF
REM Generate test workloads
CALL groovy WorkloadGenerator.groovy -s 88991  -n 10   -o build\load-10.obj    --maxthinktime 5 --maxgroup 10   -p db.properties --names data\names.txt --surnames data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 31     -n 100  -o build\load-100.obj   --maxthinktime 4 --maxgroup 10   -p db.properties --names data\names.txt --surnames data\surnames.txt

CALL groovy WorkloadGenerator.groovy -s 9447   -n 1000 -o build\load-1000a.obj --maxthinktime 3 --maxgroup 10   -p db.properties --names data\names.txt --surnames data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 245375 -n 1000 -o build\load-1000b.obj --maxthinktime 3 --maxgroup 100  -p db.properties --names data\names.txt --surnames data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 6383   -n 1000 -o build\load-1000c.obj --maxthinktime 3 --maxgroup 1000 -p db.properties --names data\names.txt --surnames data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 32549  -n 1000 -o build\load-1000d.obj --maxthinktime 3 --maxgroup 10   -p db.properties --names data\names.txt --surnames data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 133    -n 1000 -o build\load-1000e.obj --maxthinktime 3 --maxgroup 100  -p db.properties --names data\names.txt --surnames data\surnames.txt
CALL groovy WorkloadGenerator.groovy -s 1755   -n 1000 -o build\load-1000f.obj --maxthinktime 3 --maxgroup 1000 -p db.properties --names data\names.txt --surnames data\surnames.txt
