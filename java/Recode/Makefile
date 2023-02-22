SRCFILES := $(shell find src -name \*.java)
RESFILES := $(shell find src -type f \! -name .\* \! -name \*.java | sed s/^src\\///)
PACKAGES := $(shell find src -name \*.java | sed -E s/^src\\/\|\\/[^\\/]+$$//g | grep -v \\.java$$ | sort | uniq | tr / .)

all: clean bin doc recode.jar Recode.app recode.exe

eclipseall: eclipseclean recode.jar Recode.app recode.exe

clean:
	rm -rf bin
	rm -rf doc
	rm -rf *.jar
	rm -rf *.app
	rm -rf *.exe

eclipseclean:
	rm -rf *.jar
	rm -rf *.app
	rm -rf *.exe

bin: src
	mkdir -p bin
ifeq ($(origin JAVACFLAGS), undefined)
	$(eval JAVACFLAGS = $(shell dep/minJavaVerOpts))
endif
	javac $(JAVACFLAGS) -sourcepath src $(SRCFILES) -d bin
	$(foreach res,$(RESFILES),cp src/$(res) bin/$(res);)
	basename -a src/com/kreative/recode/maps/*.map > src/com/kreative/recode/maps/maps.index
	cp src/com/kreative/recode/maps/maps.index bin/com/kreative/recode/maps/

doc: src
	mkdir -p doc
	javadoc $(JAVADOCFLAGS) -sourcepath src $(PACKAGES) -d doc

recode.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST.MF recode.jar -C bin com/kreative/recode
	chmod +x recode.jar

Recode.app: Recode-Pre10.15.app Recode-MacOS10.15.app Recode-MacOS11.0.app

Recode-Pre10.15.app: dep recode.jar
	mkdir -p Recode-Pre10.15.app/Contents/MacOS
	mkdir -p Recode-Pre10.15.app/Contents/Resources/Java
	cp -f dep/PkgInfo Recode-Pre10.15.app/Contents/PkgInfo
	cp -f dep/Info.plist Recode-Pre10.15.app/Contents/Info.plist
	cp -f dep/universalJavaApplicationStub-Pre10.15 Recode-Pre10.15.app/Contents/MacOS/Recode
	cp -f dep/Recode.icns Recode-Pre10.15.app/Contents/Resources
	cp -f recode.jar Recode-Pre10.15.app/Contents/Resources/Java

Recode-MacOS10.15.app: dep recode.jar
	mkdir -p Recode-MacOS10.15.app/Contents/MacOS
	mkdir -p Recode-MacOS10.15.app/Contents/Resources/Java
	cp -f dep/PkgInfo Recode-MacOS10.15.app/Contents/PkgInfo
	cp -f dep/Info.plist Recode-MacOS10.15.app/Contents/Info.plist
	cp -f dep/universalJavaApplicationStub-MacOS10.15 Recode-MacOS10.15.app/Contents/MacOS/Recode
	cp -f dep/Recode.icns Recode-MacOS10.15.app/Contents/Resources
	cp -f recode.jar Recode-MacOS10.15.app/Contents/Resources/Java

Recode-MacOS11.0.app: dep recode.jar
	mkdir -p Recode-MacOS11.0.app/Contents/MacOS
	mkdir -p Recode-MacOS11.0.app/Contents/Resources/Java
	cp -f dep/PkgInfo Recode-MacOS11.0.app/Contents/PkgInfo
	cp -f dep/Info.plist Recode-MacOS11.0.app/Contents/Info.plist
	cp -f dep/universalJavaApplicationStub-MacOS11.0 Recode-MacOS11.0.app/Contents/MacOS/Recode
	cp -f dep/Recode.icns Recode-MacOS11.0.app/Contents/Resources
	cp -f recode.jar Recode-MacOS11.0.app/Contents/Resources/Java

recode.exe: dep recode.jar
ifeq ($(LAUNCH4J_HOME),)
	echo 'Please set $$LAUNCH4J_HOME environment variable to compile exe.'
else
	cd "$(LAUNCH4J_HOME)" ; java -Dlaunch4j.tmpdir=/tmp/ -jar "$(LAUNCH4J_HOME)/launch4j.jar" "$(PWD)/dep/Recode.xml"
endif

.PHONY: all eclipseall clean eclipseclean Recode.app
