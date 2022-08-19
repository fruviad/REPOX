

grep 'static final LogManager' `find . -name *java -print 2>/dev/null` 2>/dev/null | cut -f 1 -d ':' | sort | uniq | while read FILENAME
do
    sed -e 's/static final LogManager/static final Logger/g' < $FILENAME > /tmp/1.txt
    mv /tmp/1.txt $FILENAME
    
done




