PROGNAME=$0
BINDIR=`python -c "import os;print os.path.abspath('.')"`

echo "[$PROGNAME]installing index.html..."
rm -rf ~/index.html
ln -s $BINDIR/index.html ~/index.html

echo "[$PROGNAME]installing hosts..."
sudo rm -rf /etc/hosts
sudo ln -s $BINDIR/hosts /etc/hosts

mkdir -p $HOME/.ssh
echo "[$PROGNAME]installing ssh-config..."
rm -rf $HOME/.ssh/config
ln -s $BINDIR/ssh-config $HOME/.ssh/config

echo "[$PROGNAME]installing dirlt-aws.pem..."
rm -rf $HOME/.ssh/dirlt-aws.pem
ln -s $BINDIR/dirlt-aws.pem $HOME/.ssh/dirlt-aws.pem
chmod 0600 $HOME/.ssh/dirlt-aws.pem

# private key.
echo "[$PROGNAME]installing id_rsa.pub..."
rm -rf $HOME/.ssh/id_rsa.pub
ln -s $BINDIR/id_rsa.pub $HOME/.ssh/id_rsa.pub
chmod 0600 $HOME/.ssh/id_rsa.pub

echo "[$PROGNAME]installing id_rsa..."
rm -rf $HOME/.ssh/id_rsa
ln -s $BINDIR/id_rsa $HOME/.ssh/id_rsa
chmod 0600 $HOME/.ssh/id_rsa

echo "[$PROGNAME]installing shiwen-aws_rsa.pub..."
rm -rf $HOME/.ssh/shiwen-aws_rsa.pub
ln -s $BINDIR/shiwen-aws_rsa.pub $HOME/.ssh/shiwen-aws_rsa.pub
chmod 0600 $HOME/.ssh/shiwen-aws_rsa.pub

echo "[$PROGNAME]installing shiwen-aws_rsa..."
rm -rf $HOME/.ssh/shiwen-aws_rsa
ln -s $BINDIR/shiwen-aws_rsa $HOME/.ssh/shiwen-aws_rsa
chmod 0600 $HOME/.ssh/shiwen-aws_rsa

# maven settings.
mkdir -p $HOME/.m2
echo "[$PROGNAME]installing mvn-settings.xml..."
rm -rf $HOME/.m2/settings.xml
ln -s $BINDIR/mvn-settings.xml $HOME/.m2/settings.xml

echo "[$PROGNAME]installing .shrc"
rm -rf $HOME/.shrc
ln -s $BINDIR/.shrc $HOME/.shrc

echo "[$PROGNAME]installing .oh-my-zsh"
rm -rf $HOME/.oh-my-zsh
ln -s $BINDIR/.oh-my-zsh $HOME/.oh-my-zsh

echo "[$PROGNAME]installing .zshrc"
rm -rf $HOME/.zshrc
ln -s $BINDIR/.zshrc $HOME/.zshrc

TARGETS=".emacs .emacs.d .vpn-umeng-ubuntu-workpc .vpn-umeng-mba .vpn-umeng-ubuntu-homepc"
for target in $TARGETS
do
  echo "[$PROGNAME]installing $target..."
  rm -rf $HOME/$target
  ln -s $BINDIR/$target $HOME/$target
done

TARGETS="2utf8 cxxindent \
oprof mysqldb syslog \
local-install org2twiki \
 gc pom-create vpn-umeng-ubuntu-workpc vpn-umeng-mba vpn-umeng-ubuntu-homepc vpn-dirlt-com vpn-shiwen-aws \
vpn-dirlt-aws proc-netstat rhs go-doc-server pcrypt einstall uinstall clj lein"

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

