SHELL = /bin/bash

SRC = $(wildcard *.cc)
EXE = $(basename $(SRC))

CXXFLAGS += -g3 -I. -Os -pg -Wall
LDFLAGS += -lpthread

CPUS = 256
N    = 600

default: $(EXE)

all:	default $(EXE).eps

clean:
	$(RM) $(EXE) *~ core.* *.dat *.eps *.gp *.log callgrind.out.* gmon.out

%.dat:	%
	$(RM) $@
	for (( n=50 ; n<=$(N) ; n+=50 )); do \
		msg=$$n; \
		for (( p=1 ; p<=$(CPUS) ; p*=2 )); do \
			/usr/bin/time -f "\t %U %S %e" -o $*.tmp ./$* $$n $$n $$p;\
			msg+=$$(echo -en "\t $$(tail -1 $*.tmp)"); \
		done; \
		echo $$msg; \
		echo $$msg >> $*.dat; \
	done
	$(RM) $*.tmp

%.eps:	%.dat
	echo set key top left > matrix.gp
	echo set xlabel \"matrix size \(NxN\)\" >> matrix.gp
	echo set terminal postscript color eps >> matrix.gp
	#echo set log y >> matrix.gp
	echo set out \"matrix.eps\" >> matrix.gp
	echo set ylabel \"user + system time \(in seconds\)\" >> matrix.gp
	echo -n "plot " >> matrix.gp
	u=2; s=3; e=4; \
	comma=""; \
	for (( p=1 ; p<=$(CPUS) ; p*=2 )); do \
		echo -n $$coma \"matrix.dat\" u 1:\(\$$$$u + \$$$$s\) t \"$$p\" w lp $$p >> matrix.gp; \
		let "u += 3"; let "s += 3"; let "e += 3"; \
		coma=","; \
	done
	echo >> matrix.gp
	echo set out \"elapsed.eps\" >> matrix.gp
	echo set ylabel \"elapsed time \(in seconds\)\" >> matrix.gp
	echo -n "plot " >> matrix.gp
	u=2; s=3; e=4; \
	comma=""; \
	for (( p=1 ; p<=$(CPUS) ; p*=2 )); do \
		echo -n $$coma \"matrix.dat\" u 1:\(\$$$$e\) t \"$$p\" w lp $$p >> matrix.gp; \
		let "u += 3"; let "s += 3"; let "e += 3"; \
		coma=","; \
	done
	echo >> matrix.gp
	echo set out \"comparison.eps\" >> matrix.gp
	echo set ylabel \"time \(in seconds\)\" >> matrix.gp
	echo -n plot \"matrix.dat\" u 1:\(\$$2 + \$$3\) t \"user+system \(1 hebra\)\" w lp 1, \"matrix.dat\" u 1:\(\$$4\) t \"elapsed \(1 hebra\)\" w lp 2, \"matrix.dat\" u 1:\(\$$5 + \$$6\) t \"user+system \(2 hebras\)\" w lp 3, \"matrix.dat\" u 1:\(\$$7\) t \"elapsed \(2 hebras\)\" w lp 4  >> matrix.gp; \
	gnuplot matrix.gp

val:	$(EXE)
	n=256; \
	for (( p=1 ; p<=$(CPUS) ; p*=2 )); do \
		echo "valgrind --> matrix $${n}x$$n $$p"; \
		valgrind --demangle=yes --dump-instr=yes --tool=callgrind --trace-jump=yes ./matrix $$n $$n $$p; \
		mv callgrind.out.* matrix-$${n}x$${n}-$${p}.log; \
	done
	echo "ahora usa kcachegrind con... " *.log 
