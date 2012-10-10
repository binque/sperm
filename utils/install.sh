PROGNAME=$0
BINDIR=`python -c "import os;print os.path.abspath('.')"`

TARGETS=".bashrc .emacs .emacs.d .vpn-umeng-shijihulian .vpn-umeng-zhaowei"
for target in $TARGETS
do
  echo "[$PROGNAME]installing $target..."
  rm -rf $HOME/$target
  ln -s $BINDIR/$target $HOME/$target
done

echo "[$PROGNAME]installing index.html..."
sudo rm -rf /index.html
sudo ln -s $BINDIR/index.html /index.html

echo "[$PROGNAME]installing hosts..."
sudo rm -rf /etc/hosts
sudo ln -s $BINDIR/hosts /etc/hosts

echo "[$PROGNAME]installing ssh-config..."
sudo rm -rf $HOME/.ssh/config
sudo ln -s $BINDIR/ssh-config $HOME/.ssh/config

echo "[$PROGNAME]installing .bash_profile"
rm -rf $HOME/.bash_profile
ln -s $BINDIR/.bashrc $HOME/.bash_profile

TARGETS="2utf8 cxxindent \
oprof mysqldb syslog \
local-install org2twiki \
 gc pom-create vpn-umeng vpn-gfw \
rhs pcrypt einstall uinstall"

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

