all: build clean

build:
	mvn --quiet package
	cp target/pdfbox-interfaces*.jar ../inst/java

dependencies:
	mvn dependency:copy-dependencies -DoutputDirectory=dependencies
	cp dependencies/* ../inst/java

clean:
	mvn clean
