#!/bin/bash

echo -e "\n=========="
echo -e "Fetching sortiment.xml...\n"
if [[ -f "src/main/resources/sortiment.xml" ]]
then
    echo -e "Removing old XML"
    rm src/main/resources/sortiment.xml
fi

if [ ! -d "src/main/resources" ]
then
    echo -e "Missing directory resources/ . Creating it."
    mkdir src/main/resources
fi
echo -e "\n\n"
wget 'https://www.systembolaget.se/api/assortment/products/xml' -O src/main/resources/sortiment.xml
echo -e "\nDone fetching xml. Saved as src/main/resources/sortiment.xml"
sleep 1
echo -e "==========\n"
echo -e "\n=========="
echo -e "Fixing the XML indentation so it becomes human readable...\n\n"
tidy -indent -utf8 -xml src/main/resources/sortiment.xml > src/main/resources/formatted.xml
mv src/main/resources/formatted.xml src/main/resources/sortiment.xml
echo -e "\nDone fixing the xml indentation."
echo -e "==========\n\n"
echo -e "Inserting files to database, please wait"
mvn exec:java -Dexec.mainClass=se.gu.ait.sbserver.main.TestSQLInsertExporter -DProductLine=INSERT
echo -e "No more products to add, closing down"
