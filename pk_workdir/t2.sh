grep 'final Logger' `find . -name *java -print 2>/dev/null` | cut -f 1 -d ':' | while read FILENAME
do
    sed -e 's/final Logger/final LogManager/g' < $FILENAME > 2.txt
    mv $FILENAME $FILENAME.bak
    mv 2.txt $FILENAME
done

