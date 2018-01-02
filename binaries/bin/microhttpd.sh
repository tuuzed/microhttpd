export path=$PATH:../lib:
java -jar ../lib/microhttpd.jar $*
read -p "Press any key to continue"