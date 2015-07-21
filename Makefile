ssp:
	javac ssp.java FHeap.java GraphVertex.java
routing:
	javac GraphVertex.java routing.java TrieDataStruct.java ssp.java FHeap.java

clean:
	rm -rf *.class
