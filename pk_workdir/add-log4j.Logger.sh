#!/bin/bash


grep -R import * 2>/dev/null | grep 'log4j.Log' | cut -f 1 -d ':' | while read FILENAME
do
   sed -e 's/import org.apache.logging.log4j.LogManager;/import org.apache.logging.log4j.LogManager;\
import org.apache.logging.log4j.Logger;/g' $FILENAME > /tmp/2.dat
   mv /tmp/2.dat $FILENAME
done
