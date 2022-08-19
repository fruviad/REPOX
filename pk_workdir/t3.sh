grep 'org.apache.logging.log4j.Logger' `find . -name *java -print 2>/dev/null` | cut -f 1 -d ':' | while read FILENAME
do
    sed -e 's/org.apache.logging.log4j.Logger/org.apache.logging.log4j.LogManager/g' < $FILENAME > 2.txt
    mv $FILENAME $FILENAME.bak
    mv 2.txt $FILENAME
done

