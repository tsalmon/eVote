SRC = scala
SOURCES = ./Group.scala	./District.scala	./Electable.scala	./Voting.scala	./ElectableGroup.scala	./Person.scala	./Candidate.scala	./VotingPaper.scala	./Elections.scala	./Elector.scala	./ElectionsManager.scala	./Statistics.scala	./ElectionsResultProcessing.scala	./ElectionsTest.scala
S = scala
SC = scalac
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
	@$(RM) $(SRC)/*.class