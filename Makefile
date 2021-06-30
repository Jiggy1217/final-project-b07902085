run:

	javac -cp src/ -d out/ src/*/*/*/*.java
	cp -r src/assets out/
	java -cp out/ com.lovingshiba.view.Window
clean:

	rm -r out/