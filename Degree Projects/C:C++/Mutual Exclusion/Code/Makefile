SHELL = /bin/bash # para ubuntu

SRC = $(wildcard *.cc)
EXE = $(basename $(SRC))

CXXFLAGS = -O3 -Wall
LDFLAGS  = -lpthread

default: $(EXE)

all:	stat

clean:
	$(RM) $(EXE) core.* *~ *.log *.tmp

stat:	$(EXE)
	@echo "atención: mide el tamaño de un pthread_t y cambia grep si != 10"
	@echo -e "mensajes\tbien\ttotal\thebras\treal\tuser\tsys"
	@echo "---------------------------------------------------------------"
	@for i in $(EXE); do \
		$$(/usr/bin/time -f "%e\t%U\t%S" -o ./$$i.tmp ./$$i  2> /dev/null > ./$$i.log); \
		echo -n $$i; \
		for (( s=$${#i} ; s<9; ++s )); do echo -n " "; done; \
		echo -e "\t$$(cat ./$$i.log | grep -e '^\[[0-9]\{10\}\]: 0123456789$$' | wc -l)\t$$(cat ./$$i.log | wc -l)\t$$(cat ./$$i.log | grep -e '^\[[0-9]\{10\}\]: 0123456789$$' | cut -d ":" -f 1 | sort | uniq | wc -l)\t$$(tail -1 ./$$i.tmp)";\
		$(RM) ./$$i.tmp; \
	done

valgrind: $(EXE)
	for i in $(EXE); do \
		echo "valgrind --> $$i"; \
		valgrind --demangle=yes --dump-instr=yes --tool=callgrind --trace-jump=yes ./$$i;\
		mv callgrind.out.* $$i.log;\
	done
	echo "ahora usa kcachegrind con... " *.log 
