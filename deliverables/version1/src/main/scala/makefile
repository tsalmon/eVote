SRC = ./
SOURCES= ./Elections/Group.scala	./Elections/District.scala	./Elections/Electable.scala	./Elections/Voting.scala	./Elections/ElectableGroup.scala	./Elections/Person.scala	./Elections/Candidate.scala ./Elections/NotificationCenter.scala	./Elections/VotingPaper.scala	./Elections/Elections.scala	./Elections/Elector.scala	./Elections/ElectionsManager.scala	./Statistics/Statistics.scala	./ElectionsResultProcessing/ElectionsResultProcessing.scala	./ElectionsTest/ElectionsTest.scala

S = scala
SC = scalac -d "./"
TARGET = target
CP = $(TARGET):scalatest.jar
SPEC = scala.RomanSpec

compile: $(SOURCES:.scala=.class)

%.class: %.scala
	@echo "Compiling $*.scala.."
	@$(SC) $*.scala

test: compile
	@$(S) -cp $(CP) org.scalatest.tools.Runner -p . -o -s $(SPEC)

clean:
	@$(RM) $(SRC)/*/*.class
