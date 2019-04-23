# Hunter Damron
# Copyright 2018

BUILD=build
SRC=src

LABS=$(patsubst $(SRC)/%,%,$(wildcard $(SRC)/*))
RUNLABS=$(addprefix run,$(LABS))

.PHONY: all $(LABS) $(RUNLABS)

all: $(LABS)
run: $(RUNLABS)

$(foreach lab,$(LABS),$(eval $(lab): $(BUILD)/$(lab)/Main.class))

# Pattern rule for compiling from SRC to BUILD
$(BUILD)/%/Main.class: $(SRC)/%/Main.java | $(BUILD)
	javac -d $(BUILD)/ -sourcepath $(dir $<) $<

$(RUNLABS):
	java -cp $(BUILD)/ $(patsubst run%,%,$@).Main $(args)

$(BUILD):
	mkdir -p $@

clean:
	@rm -rf $(BUILD)
