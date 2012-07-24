;;; .emacs

;;; common code.
;; sudo apt-get install emacs-goodies-el
(require 'xml-parse)
(autoload 'make-regexp "make-regexp"
  "Return a regexp to match a string item in STRINGS.")
(autoload 'make-regexps "make-regexp"
  "Return a regexp to REGEXPS.")
(require 'syntax)
(setq load-path (cons "~/.emacs.d/" load-path))

;;; perference.
;; (setq inhibit-default-init t)
(when (fboundp 'global-font-lock-mode) 
  (global-font-lock-mode t))
(setq frame-title-format (concat  "emacs@%b" system-name))
;;(normal-erase-is-backspace-mode)
(setq transient-mark-mode t)
(setq column-number-mode t)
(setq default-fill-column 80)
(setq hl-line-mode t)
;; (setq make-backup-files nil) 
(setq backup-directory-alist (quote (("." . "~/.backups"))))
(setq bookmark-save-flag 1) 
(setq default-major-mode 'text-mode)
;;(add-hook 'text-mode-hook 'turn-on-auto-fill) ;;自动换行.
(setq inhibit-startup-message t)
(setq inhibit-splash-screen t)
(setq x-select-enable-clipboard t) ;;允许复制到外部剪贴板
(setq default-major-mode 'text-mode)
(add-to-list 'auto-mode-alist '("\\.el\\'" . emacs-lisp-mode))
(setq initial-major-mode 'emacs-lisp-mode)
(setq visible-bell nil)
(setq kill-ring-max 200)
(show-paren-mode t)
(setq show-paren-style 'parentheses)
(mouse-avoidance-mode 'animate)
(display-time)

;; turn off tool-bar, menu-bar, and scroll-bar
(tool-bar-mode 0)
(menu-bar-mode 1)
(scroll-bar-mode -1)


;; Most of the settings are from sk8er's HomePage
;; (start-server)
(setq indent-tabs-mode nil)
(setq tab-width 4)
(setq tab-stop-list ())
(setq sentence-end-double-space nil)

(setq scroll-margin 3
      scroll-conservatively 10000)

(auto-image-file-mode t)

(put 'set-goal-column 'disabled nil)
(put 'narrow-to-region 'disabled nil)
(put 'upcase-region 'disabled nil)
(put 'downcase-region 'disabled nil)
(put 'LaTeX-hide-environment 'disabled nil)
(put 'narrow-to-page 'disabled nil)

(setq version-control t)
(setq kept-new-versions 3)
(setq delete-old-versions t)
(setq kept-old-versions 2)
(setq dired-kept-versions 1)
(setq backup-directory-alist (quote (("." . "~/.backups"))))
;; (setq ansi-color-for-comint-mode t)
(setq user-full-name "dirtysalt") 
(setq user-mail-address "dirtysalt1987@gmail.com")

(setq dired-recursive-copies 'top)
(setq dired-recursive-deletes 'top)

;;; htmlize.
(require 'htmlize)

;;; recentf.
(require 'recentf)
(recentf-mode 1)

;;; calendar.
(add-hook 'diary-display-hook 'fancy-diary-display)
(add-hook 'list-diary-entries-hook 'sort-diary-entries t)

;;; go.
(require 'go-mode-load)

;;; clojure.
(require 'clojure-mode)

;;; google c style.
(require 'google-c-style)
(setq c-basic-offset 2)
(setq c-default-style "java")
(add-hook 'c-mode-common-hook 'google-set-c-style)
(add-hook 'c-mode-common-hook 'google-make-newline-indent)
(setq auto-mode-alist  (append '(("\\.h\\'" . c++-mode)) 
                               '(("\\.hpp\\'" . c++-mode))
                               '(("\\.c\\'" . c++-mode)) 
                               '(("\\.cc\\'" . c++-mode)) 
                               '(("\\.cpp\\'" . c++-mode)) 
                               '(("\\.java\\'".c++-mode))
                               auto-mode-alist))
(setq-default indent-tabs-mode nil)
(setq-default nuke-trailing-whitespace-p t)
(setq tab-width 2)

;;; doxymacs.
;; sudo apt-get install doxymacs
;; - Default key bindings are:
;;   - C-c d ? will look up documentation for the symbol under the point.
;;   - C-c d r will rescan your Doxygen tags file.
;;   - C-c d f will insert a Doxygen comment for the next function.
;;   - C-c d i will insert a Doxygen comment for the current file.
;;   - C-c d ; will insert a Doxygen comment for the current member.
;;   - C-c d m will insert a blank multiline Doxygen comment.
;;   - C-c d s will insert a blank singleline Doxygen comment.
;;   - C-c d @ will insert grouping comments around the current region.
(require 'doxymacs)
(add-hook 'c-mode-common-hook 'doxymacs-mode)
(add-hook 'python-mode-hook 'doxymacs-mode)
(add-hook 'html-mode-hook 'doxymacs-mode)
(defun my-doxymacs-font-lock-hook ()
  (if (or (eq major-mode 'c-mode) 
          (eq major-mode 'c++-mode)
          (eq major-mode 'python-mode)
          (eq major-mode 'java-mode))
      (doxymacs-font-lock)))
(add-hook 'font-lock-mode-hook 'my-doxymacs-font-lock-hook)

;;; hippie-expand.
(setq hippie-expand-try-functions-list
      '(try-expand-dabbrev-visible 
        try-expand-dabbrev 
        try-expand-dabbrev-all-buffers 
        try-expand-dabbrev-from-kill 
        try-complete-file-name-partially 
        try-complete-file-name 
        try-expand-all-abbrevs 
        try-expand-list 
        try-expand-line 
        try-complete-lisp-symbol-partially 
        try-complete-lisp-symbol
        try-expand-whole-kill))


;;; color-theme. http://alexpogosyan.com/color-theme-creator
(require 'color-theme)
(eval-after-load "color-theme"
  '(progn 
     (color-theme-initialize)
     (color-theme-billw)))
;; (eval-after-load "color-theme"
;;   '(progn 
;;      (color-theme-initialize)
;;      (color-theme-arjen)))


;;; org-mode. I have to include it because I've changed the code.
;; BEGIN_VERSE TODO(dirlt):?
;; BEGIN_QUOTE
;; BEGIN_CENTER
;; C-c C-n //下一个标题
;; C-c C-p //上一个标题
;; C-c C-f //同级下一个标题
;; C-c C-b //同级上一个标题
;; C-c C-u //高一层标题
;; C-c C-o //打开连接
;; C-c C-l //查看连接
;; C-cl // 保存链接 org-store-link
;; C-cxa // 日程安排 org-agenda
;;(setq load-path (cons "~/.emacs.d/org-mode/lisp" load-path))
;;(setq load-path (cons "~/.emacs.d/org-mode/contrib/lisp" load-path))
(setq load-path (cons "~/.emacs.d/org-7.8.11/lisp" load-path))
(setq load-path (cons "~/.emacs.d/org-7.8.11/contrib/lisp" load-path))
(require 'org-install)
(require 'org-publish)
;; (add-to-list 'auto-mode-alist '("\\.org\\'" . org-mode))
(add-to-list 'auto-mode-alist '("\\.org$" . org-mode))
(add-hook 'org-mode-hook 'turn-on-font-lock)
(add-hook 'org-mode-hook 
          (lambda () (setq truncate-lines nil)))
(setq org-log-done t)
;;(setq org-agenta-files (file-expand-wildcards "~/github/sperm/essay/Plan.org"))
(setq org-agenta-files "~/github/sperm/essay/Plan.org")
(setq org-export-have-math nil)
(setq org-use-sub-superscripts (quote {}))
(setq org-publish-project-alist
      '(("blog"
         :base-directory "~/github/sperm/essay"
         :publishing-directory "~/github/sperm/essay/www/"
         :section-numbers 't
         :table-of-contents 't)))
;;auto indent
(setq org-startup-indented t)
;; arrange for the clock information to persist across Emacs sessions
(setq org-clock-persist t)
(org-clock-persistence-insinuate)

(define-key global-map "\C-cl" 'org-store-link)
(define-key global-map "\C-cxa" 'org-agenda)

;;; yacc-mode.
(require 'yacc-mode)
(add-to-list 'auto-mode-alist '("\\.l\\'" . yacc-mode))
(add-to-list 'auto-mode-alist '("\\.y\\'" . yacc-mode))

;;; cmake-mode.
(require 'cmake-mode)

;;; auto-complete.
(setq load-path (cons "~/.emacs.d/auto-complete" load-path))
(setq ac-dictionary-directories "~/.emacs.d/auto-complete/ac-dict")
(setq ac-dictionary-directories (cons "~/.emacs.d/auto-complete/dict" ac-dictionary-directories))
(require 'auto-complete-config)
(require 'auto-complete)
(global-auto-complete-mode 1)
(setq ac-modes (append ac-modes '(text-mode
                                  change-log-mode
                                  org-mode
                                  c-mode                          
                                  c++-mode
                                  java-mode
                                  python-mode)))

;;; python-mode.
;; sudo apt-get install python-mode
(require 'python-mode)

;;; ropemacs. I feel some bugs in it so I disable it first.
;;; sudo apt-get install python-ropemacs
;; (autoload 'pymacs-apply "pymacs")
;; (autoload 'pymacs-call "pymacs")
;; (autoload 'pymacs-eval "pymacs" nil t)
;; (autoload 'pymacs-exec "pymacs" nil t)
;; (autoload 'pymacs-load "pymacs" nil t)
;; (pymacs-load "ropemacs" "rope-")
;; (setq ropemacs-enable-autoimport t)

;;; php-mode
;; sudo apt-get install php-elisp
(require 'php-mode)

;; ;;; cedet.
;; (setq load-path (cons "~/.emacs.d/cedet-1.1/common" load-path))
;; (require 'cedet)

;;; ido.
(require 'ido)
(ido-mode t)

;;; anything. I think it's almost useless in front of ido.
;; sudo apt-get install anything-el
(require 'anything)
(require 'anything-config)
(global-set-key "\C-ca" 'anything)

;;; cscope.
;;; NOTE(dirlt):但是其实索引效果没有那么好，cscope对于C支持很好，对C++就已经有点吃力了。
;; sudo apt-get install cscope-el
(require 'xcscope)
;; C-c s a //设定初始化的目录，一般是你代码的根目录
;; C-s s I //对目录中的相关文件建立列表并进行索引
;; C-c s s //序找符号
;; C-c s g //寻找全局的定义
;; C-c s c //看看指定函数被哪些函数所调用
;; C-c s C //看看指定函数调用了哪些函数
;; C-c s e //寻找正则表达式
;; C-c s f //寻找文件
;; C-c s i //看看指定的文件被哪些文件include
;; C-c s u //回到上一个跳转点(pop up mark)
;; C-c s p //回到上一个symbol
;; C-c s P //回到上一个文件
;; C-c s n //下一个symbol
;; C-c s N //下一个文件
(setq cscope-do-not-update-database t) ;; don't need to update database
;; cscope仅仅对于C/C++文件有用,对于其他文件的话可以使用etags
;; etags FILES这样会索引FILES生成TAGS文件
;; M-x visit-tags-table //提示载入TAGS文件
;; M-. //查找相应函数定义
;; C-u M-. //如果错误的话,那么查找下一个
;; M-* //返回之前的位置

;;; ibuffer.
(require 'ibuffer)
(global-set-key (kbd "C-x C-b") 'ibuffer)

;;; ibus.
;; sudo apt-get install ibus-el
(require 'ibus)
(add-hook 'after-init-hook 'ibus-mode-on)
;; ;; Use C-SPC for Set Mark command
;; (ibus-define-common-key ?\C-\s nil)
;; Use C-/ for Undo command
(ibus-define-common-key ?\C-/ nil)
(global-set-key [(shift)] 'ibus-toggle)
;; Change cursor color depending on IBus status
(setq ibus-cursor-color '("red" "blue" "limegreen"))

;;; desktop.
(require 'desktop)
(desktop-save-mode t)

;;; session.
(require 'session)
(add-hook 'after-init-hook 'session-initialize)

;;; nxml mode
;; 通常来说使用C-c C-f/ C-c C-b / C-c C-i比较多.
;; (defvar nxml-mode-map
;;       (let ((map (make-sparse-keymap)))
;;         (define-key map "\M-\C-u"  'nxml-backward-up-element)
;;         (define-key map "\M-\C-d"  'nxml-down-element)
;;         (define-key map "\M-\C-n"  'nxml-forward-element)
;;         (define-key map "\M-\C-p"  'nxml-backward-element)
;;         (define-key map "\M-{"     'nxml-backward-paragraph)
;;         (define-key map "\M-}"     'nxml-forward-paragraph)
;;         (define-key map "\M-h"     'nxml-mark-paragraph)
;;         (define-key map "\C-c\C-f" 'nxml-finish-element)
;;         (define-key map "\C-c\C-b" 'nxml-balanced-close-start-tag-block)
;;         (define-key map "\C-c\C-i" 'nxml-balanced-close-start-tag-inline)
;;         (define-key map "\C-c\C-x" 'nxml-insert-xml-declaration)
;;         (define-key map "\C-c\C-d" 'nxml-dynamic-markup-word)
;;         (define-key map "\C-c\C-u" 'nxml-insert-named-char)
;;         (define-key map "/"        'nxml-electric-slash)
;;         (define-key map [C-return] 'nxml-complete) 
;;         (when nxml-bind-meta-tab-to-complete-flag
;;           (define-key map "\M-\t"  'nxml-complete))
;;         map)
;;       "Keymap used by NXML Mode.")
(setq load-path (cons "~/.emacs.d/nxml-mode-20041004" load-path))
(require 'nxml-mode)
(setq auto-mode-alist
        (cons '("\\.\\(xml\\|xsl\\|rng\\|xhtml\\|html\\|htm\\)\\'" . nxml-mode)
              auto-mode-alist))

;;; multi-term.
(require 'multi-term)
(setq multi-term-program "/bin/bash")
(setq multi-term-buffer-name "multi-term")
(global-set-key "\C-x." 'multi-term)
(global-set-key [(f12)] 'multi-term)
(setq multi-term-dedicated-select-after-open-p t)

;;; protobuf-mode.
(require 'protobuf-mode)
(setq auto-mode-alist (cons '("\\.proto\\'" . protobuf-mode) auto-mode-alist))

;;; markdown-mode.
(require 'markdown-mode)
(setq auto-mode-alist (cons '("\\.md\\'" . markdown-mode) auto-mode-alist))

;;; package.
;; M-x package-list-packages
(require 'package)
;; Add the original Emacs Lisp Package Archive
(add-to-list 'package-archives
             '("elpa" . "http://tromey.com/elpa/"))
;; Add the user-contributed repository
(add-to-list 'package-archives
             '("marmalade" . "http://marmalade-repo.org/packages/"))

;;; global keybindings.
(global-set-key "\M-g" 'goto-line)
(global-set-key "\M-m" 'compile)
(global-set-key "\M-/" 'hippie-expand)
(global-set-key "\C-xp" 'previous-error) 
(global-set-key "\C-xn" 'next-error)
(global-set-key [(f8)] 'speedbar-get-focus)
(global-set-key [(f9)] 'list-bookmarks)
(global-set-key [(f10)] 'bookmark-set)
(global-set-key "\C-chdf" 'describe-function)
(global-set-key "\C-chdv" 'describe-variable)
(global-set-key "\C-chdk" 'describe-key)
(global-set-key (quote [C-return]) (quote set-mark-command))
(global-set-key "\C-c;" 'comment-or-uncomment-region)

;;; encoding.
(set-language-environment "utf-8")
(setq current-language-environment "UTF-8")
(setq locale-coding-system 'utf-8)
(set-terminal-coding-system 'utf-8)
(set-keyboard-coding-system 'utf-8)
(set-selection-coding-system 'utf-8)
(prefer-coding-system 'utf-8)

;;; default browser.
;; 使用chromium浏览器打开链接
(setq browse-url-generic-program 
      (executable-find "chromium-browser")
      browse-url-browser-function 'browse-url-generic)

;;; NOTE(dirlt):比较感慨就是基本上所有Web方面的东西在Emacs上面都会显得很烂。
;;; 并且中文处理能力也非常查。基本上强项还是文本编辑。

;; ;;; emacs-w3m. 浏览网页的效果可谓是相当的烂！
;; (require 'w3m)

;; ;;; newsticker. 显示效果还是相当的不好！
;; (autoload 'newsticker-start "newsticker" "Emacs Newsticker" t)
;; (autoload 'newsticker-show-news "newsticker" "Emacs Newsticker" t)
;; (setq newsticker-dir "~/.newsticker/")
;; (setq newsticker-url-list-defaults nil)
;; (setq newsticker-automatically-mark-visited-items-as-old t)
;; (setq newsticker-retrieval-interval 600)
;; (setq newsticker-treeview-treewindow-width 40)
;; (setq newsticker-treeview-listwindow-height 30)
;; (setq newsticker-url-list
;;       '(("solitdot"
;;          "http://solidot.org/index.rss"         
;;          nil nil nil)))

;; ;;; evernote.
;; (require 'evernote-mode)
;; ;; (setq evernote-username "<your evernote user name>") ; optional: you can use this username as default.
;; (setq evernote-username "dirtysalt1987@gmail.com")
;; (setq evernote-enml-formatter-command '("w3m" "-dump" "-I" "UTF8" "-O" "UTF8")) ; option
;; (global-set-key "\C-cec" 'evernote-create-note)
;; (global-set-key "\C-ceo" 'evernote-open-note)
;; (global-set-key "\C-ces" 'evernote-search-notes)
;; (global-set-key "\C-ceS" 'evernote-do-saved-search)
;; (global-set-key "\C-cew" 'evernote-write-note)
;; (global-set-key "\C-cep" 'evernote-post-region)
;; (global-set-key "\C-ceb" 'evernote-browser)

;;; dvorak-mode.
;; Define M-2 (Escape then "2") to turn on Dvorak mode
;; Define M-1 to turn it off
;;      (you may want to set your own keys)
(load "dvorak-mode.el")

;;; magit.
;; Getting started with Magit is really easy:

;; M-x magit-status to see git status, and in the status buffer:
;; s to stage files
;; c to commit (type in your commit message then C-c C-c to save the message and commit)
;; b b to switch to a branch
;; Other handy keys:

;; P P to do a git push
;; F F to do a git pull
;; try to press TAB
;; sudo apt-get install magit
;; http://zagadka.vm.bytemark.co.uk/magit/magit.html
(require 'magit)
(global-set-key '[f6] 'magit-status)

;;; ace-jump-mode.
(autoload 'ace-jump-mode "ace-jump-mode" "Emacs AceJump minor mode" t)
;; (define-key global-map (kbd "C-c SPC") 'ace-jump-mode)
(define-key global-map (kbd "M-`") 'ace-jump-mode)

;; activate the appt
(appt-activate 1)
(setq appt-display-format 'window)
