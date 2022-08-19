

grep 'private static LogManager' `find . -name *java -print 2>/dev/null` 2>/dev/null | cut -f 1 -d ':' | sort | uniq | while read FILENAME
do
    sed -e 's/private static LogManager/private static Logger/g' < $FILENAME > /tmp/1.txt
    mv /tmp/1.txt $FILENAME
    
done




