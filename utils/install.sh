PROGNAME=$0
BINDIR=`python -c "import os;print os.path.abspath('.')"`

echo "[$PROGNAME]installing index.html..."
sudo rm -rf /index.html
sudo ln -s $BINDIR/index.html /index.html

echo "[$PROGNAME]installing hosts..."
sudo rm -rf /etc/hosts
sudo ln -s $BINDIR/hosts /etc/hosts

mkdir -p $HOME/.ssh
echo "[$PROGNAME]installing ssh-config..."
rm -rf $HOME/.ssh/config
ln -s $BINDIR/ssh-config $HOME/.ssh/config

echo "[$PROGNAME]installing id_rsa.pub..."
rm -rf $HOME/.ssh/id_rsa.pub
ln -s $BINDIR/id_rsa.pub $HOME/.ssh/id_rsa.pub

echo "[$PROGNAME]installing id_rsa..."
rm -rf $HOME/.ssh/id_rsa
ln -s $BINDIR/id_rsa $HOME/.ssh/id_rsa
chmod 600 $HOME/.ssh/id_rsa

mkdir -p $HOME/.m2
echo "[$PROGNAME]installing mvn-settings.xml..."
rm -rf $HOME/.m2/settings.xml
ln -s $BINDIR/mvn-settings.xml $HOME/.m2/settings.xml

echo "[$PROGNAME]installing .bash_profile"
rm -rf $HOME/.bash_profile
ln -s $BINDIR/.bashrc $HOME/.bash_profile

TARGETS=".bashrc .emacs .emacs.d .vpn-umeng-shijihulian .vpn-umeng-zhaowei"
for target in $TARGETS
do
  echo "[$PROGNAME]installing $target..."
  rm -rf $HOME/$target
  ln -s $BINDIR/$target $HOME/$target
done

TARGETS="2utf8 cxxindent \
oprof mysqldb syslog \
local-install org2twiki \
 gc pom-create vpn-umeng vpn-gfw \
rhs gds pcrypt einstall uinstall"

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

