# Hunter Damron
# Copyright 2018

BUILD=build
SRC=src
MAIN=Lab18

all: compile
.PHONY: all mkbuild

compile: $(BUILD)/$(MAIN).class

# Pattern rule for compiling from SRC to BUILD
$(BUILD)/%.class: $(SRC)/%.java | $(BUILD)
	javac -d $(BUILD)/ -sourcepath $(SRC)/ $<

$(BUILD):
	mkdir -p $@

run: compile
	java -cp $(BUILD)/ Lab18 $(args)
