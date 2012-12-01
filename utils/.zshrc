# Path to your oh-my-zsh configuration.
ZSH=$HOME/.oh-my-zsh

# Set name of the theme to load.
# Look in ~/.oh-my-zsh/themes/
# Optionally, if you set this to "random", it'll load a random theme each
# time that oh-my-zsh is loaded.
ZSH_THEME="robbyrussell"

# Example aliases
# alias zshconfig="mate ~/.zshrc"
# alias ohmyzsh="mate ~/.oh-my-zsh"

# Set to this to use case-sensitive completion
# CASE_SENSITIVE="true"

# Comment this out to disable weekly auto-update checks
# DISABLE_AUTO_UPDATE="true"

# Change this value to set how frequently ZSH updates¬
export UPDATE_ZSH_DAYS=13

# Uncomment following line if you want to disable colors in ls
# DISABLE_LS_COLORS="true"

# Uncomment following line if you want to disable autosetting terminal title.
# DISABLE_AUTO_TITLE="true"

# Uncomment following line if you want red dots to be displayed while waiting for completion
# COMPLETION_WAITING_DOTS="true"

# Which plugins would you like to load? (plugins can be found in ~/.oh-my-zsh/plugins/*)
# Custom plugins may be added to ~/.oh-my-zsh/custom/plugins/
# Example format: plugins=(rails git textmate ruby lighthouse)
plugins=(git)

source $ZSH/oh-my-zsh.sh

# Customize to your needs...

# whether it's mac otherwise it's ubuntu.
MAC=0
if [ $HOME = "/Users/dirlt" ]
then
    MAC=1
fi

if [ $MAC = 1 ]
then
    export JAVA_HOME=/Library/Java/Home/
else
    export JAVA_HOME=/usr/lib/jvm/java-6-openjdk-amd64/
    export HADOOP_HOME=$HOME/utils/hadoop-0.20.2-cdh3u3/
    export LD_LIBRARY_PATH=$HADOOP_HOME/lib/native/Linux-amd64-64/:$LD_LIBRARY_PATH
fi

CLASSPATH=$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar:.
export CLASSPATH

PATH=$HOME/utils/bin:$JAVA_HOME/bin:/usr/local/bin:/usr/local/sbin:/usr/bin:/usr/sbin:/sbin:$PATH
MANPATH=/usr/local/share/man:/usr/share/man:$MANPATH

# most items are useless since I get apt-get install :)
PATH=$HOME/utils/graphviz/bin:$PATH
MANPATH=$HOME/utils/graphviz/share/man:$MANPATH
PATH=$HOME/utils/ant/bin:$PATH
PATH=$HOME/utils/python/bin:$PATH
MANPATH=$HOME/utils/python/share/man:$MANPATH
PATH=$HOME/utils/valgrind/bin:$PATH
PATH=$HOME/utils/protobuf/bin:$PATH
PATH=$HOME/utils/astyle/build/gcc/bin/:$PATH
PATH=$HOME/utils/flex/bin:$PATH
PATH=$HOME/utils/php/bin:$PATH
PATH=$HOME/utils/thrift/bin:$PATH
PATH=$HOME/utils/sqlite/bin:$PATH
MANPATH=$HOME/utils/sqlite/shared/man:$MANPATH
PATH=$HOME/utils/bochs/bin:$PATH
PATH=$HOME/utils/mysql/bin:$PATH
PATH=$HOME/utils/emacs/bin:$PATH
MANPATH=$HOME/utils/emacs/share/man:$MANPATH
PATH=$HOME/utils/git/bin:$PATH
PATH=$HOME/utils/git/libexec/git-core:$PATH
MANPATH=$HOME/utils/git/share/man:$MANPATH
PATH=$HOME/utils/lynx/bin:$PATH
MANPATH=$HOME/utils/lynx/man:$MANPATH
PATH=$HOME/utils/ccache/bin:$PATH
MANPATH=$HOME/utils/ccache/share/man/:$MANPATH
PATH=$HOME/utils/rar/bin:$PATH
PATH=$HOME/utils/oprofile/bin:$PATH
MANPATH=$HOME/utils/oprofile/share/man:$MANPATH
PATH=$HOME/utils/subversion/bin:$PATH
MANPATH=$HOME/utils/subversion/share/man:$MANPATH
PATH=$HOME/utils/distcc/bin:$PATH
MANPATH=$HOME/utils/distcc/shared/man:$MANPATH
PATH=$HOME/utils/tree/bin:$PATH
MANPATH=$HOME/utils/tree/man:$MANPATH
PATH=$HOME/utils/cmake/bin:$PATH
PATH=$HOME/utils/asciidoc/bin:$PATH
MANPATH=$HOME/utils/asciidoc/share/man:$MANPATH
PATH=$HOME/utils/xmlto/bin:$PATH
MANPATH=$HOME/utils/xmlto/share/man:$MANPATH
PATH=$HOME/utils/swig/bin:$PATH
MANPATH=$HOME/utils/swig/shared/man:$MANPATH
PATH=$HOME/utils/tcpdump/sbin:$PATH
MANPATH=$HOME/utils/tcpdump/share/man:$MANPATH
MANPATH=$HOME/utils/zeromq/share/man:$MANPATH
PATH=$HOME/utils/m4/bin:$PATH
PATH=$HOME/utils/autoconf/bin:$PATH
PATH=$HOME/utils/automake/bin:$PATH
MANPATH=$HOME/utils/m4/share/man:$MANPATH
MANPATH=$HOME/utils/autoconf/share/man:$MANPATH
MANPATH=$HOME/utils/automake/share/man:$MANPATH
PATH=$HOME/utils/clisp/bin:$PATH
MANPATH=$HOME/utils/clisp/share/man:$MANPATH
PATH=$HOME/github/depot_tools:$PATH
PATH=$HOME/utils/nginx/sbin:$PATH
PATH=$HOME/utils/hadoop-0.20.2-cdh3u3/bin:$PATH
PATH=$HOME/utils/hbase-0.90.4-cdh3u3/bin:$PATH
PATH=$HOME/utils/oozie-2.3.2-cdh3u3/bin:$PATH

# texmacs
TEXMACS_PATH=$HOME/utils/TeXmacs-1.0.7-static-gnu-linux/
PATH=$TEXMACS_PATH/bin:$PATH
export TEXMACS_PATH

# golang-go.
export GOROOT=$HOME/utils/go
PATH=$GOROOT/bin:$PATH
GOPATH=$HOME/go/
export GOPATH

# export 
export PATH
export MANPATH
export M2_REPO=$HOME/.m2/repository/
export LC_ALL=en_US.UTF8
export LANG=en_US.UTF8

# default value of oozie url.
export OOZIE_URL="http://localhost:11000/oozie" 

git config --global alias.lg "log --color --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit --"
git config --global color.ui true
git config --global user.name "dirtysalt"
git config --global user.email "dirtysalt1987@gmail.com"
git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.ci commit
git config --global alias.st status
git config --global alias.unstage 'reset'
git config --global alias.last 'log -1 HEAD'

alias ed="emacs" # for typo.
alias dstat='dstat -cdlmnpsy' # easy use.
alias dp1="ssh -p 16021 -l dp dp1" # authorized.
alias hadoop1="ssh -p 16021 -l yunbg hadoop1" # authorized.
alias nexus="ssh -p 16021 -l yunbg nexus" # authorized.
alias hudson="ssh -l ceshi hudson" # authorized.

ssh-add ~/.ssh/aws_rsa
ssh-add ~/.ssh/id_rsa

