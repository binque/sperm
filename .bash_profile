#!/bin/bash

BASHPF=1
if [ -f ~/.bashrc ]; then  
    # to prevent recursive execute .bashrc.
    # see also .bashrc
    if [ $BASHRC"X" == "X" ]; then
        . ~/.bashrc
    fi
fi

export DOC="$HOME/github/tpircs/essay"
export JAVA_HOME=/usr/lib/jvm/java-6-openjdk-amd64/x
export CLASSPATH=$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar:.

PATH=$JAVA_HOME/bin:/usr/local/bin:/usr/local/sbin:/usr/bin:/usr/sbin:/sbin:$PATH
PATH=$HOME/utils/bin:$PATH
PATH=$JAVA_HOME/bin:$PATH
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

export PATH
export MANPATH

export LC_ALL=zh_CN.UTF8
export LANG=zn_CN.UTF8
alias ed="emacs"

# not set ls colors.
if [ $LS_COLORS"X" == "X" ];  then
    # fi = file
    # di = directory
    # ln = soft link
    # pi = fifo file
    # so = socket file
    # bd = block (buffered) special file
    # cd = character (unbuffered) special file
    # or = symbolic link pointing to a non-existent file (orphan)
    # mi = non-existent file pointed to by a symbolic link (visible when you type ls -l)
    # ex = file which is executable (ie. has 'x' set in permissions).
    LS_COLORS="no=00:fi=00:di=01;34:ln=01;36:pi=40;33:so=01;35:do=01;35:bd=40;33;01:cd=40;33;01:or=40;31;01:su=37;41:sg=30;43:ex=01;32:*.tar=01;31:*.tgz=01;31:*.arj=01;31:*.taz=01;31:*.lzh=01;31:*.lzma=01;31:*.tlz=01;31:*.txz=01;31:*.zip=01;31:*.z=01;31:*.Z=01;31:*.dz=01;31:*.gz=01;31:*.lz=01;31:*.xz=01;31:*.bz2=01;31:*.bz=01;31:*.tbz=01;31:*.tbz2=01;31:*.tz=01;31:*.deb=01;31:*.rpm=01;31:*.jar=01;31:*.rar=01;31:*.ace=01;31:*.zoo=01;31:*.cpio=01;31:*.7z=01;31:*.rz=01;31:"
    alias ls="ls --color"
fi

alias sl='ls'
alias dstat='dstat -cdlmnpsy'
git config --global alias.lg "log --color --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit --"
git config --global color.ui true
git config --global user.name "dirtysalt"
git config --global user.email "dirtysalt1987@gmail.com"

alias u1="ssh -p 16021 -l yunbg 118.194.160.9"
alias u2="ssh -p 16021 -l yunbg 118.194.161.167"
alias u3="ssh -p 16021 -l yunbg 118.194.160.11"
alias dp1="ssh -p 16021 -l dp 211.151.139.253"
