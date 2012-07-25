PROGNAME=$0
BINDIR=`python -c "import os;print os.path.abspath('.')"`

TARGETS=".bash_profile .bashrc .emacs .emacs.d .vpn-umeng"
for target in $TARGETS
do
  echo "[$PROGNAME]installing $target..."
  rm -rf $HOME/$target
  ln -s $BINDIR/$target $HOME/$target
done
sudo rm -rf /index.html
sudo ln -s $BINDIR/index.html /index.html

TARGETS="2utf8 cxxindent \
oprof mysqldb syslog \
local-install org2twiki \
 gc pom-create vpn-umeng vpn-gfw
rhs private"

INSTALLDIR=$HOME/utils/bin
if [ ! -d $INSTALLDIR ]
then
    mkdir -p $INSTALLDIR
fi
for target in $TARGETS
do
  echo "[$PROGNAME]installing $target..."
  rm -rf $INSTALLDIR/$target
  ln -s $BINDIR/$target $INSTALLDIR/$target
done

