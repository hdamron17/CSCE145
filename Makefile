# Hunter Damron
# Copyright 2018

BUILD=build
SRC=src

LABS=$(patsubst %/,%,$(dir $(wildcard Lab*/Makefile)))
RUNLABS=$(foreach lab,$(LABS),run$(lab))
.PHONY: all run $(LABS) run%

all: $(LABS)

$(LABS):
	@echo Entering $@
	@$(MAKE) -C $@
	@echo Exiting $@

$(RUNLABS):
	@echo Entering $@
	@$(MAKE) -C $(patsubst run%,%,$@)
	@$(MAKE) -C $(patsubst run%,%,$@) run
	@echo Exiting $@

run: $(RUNLABS)
