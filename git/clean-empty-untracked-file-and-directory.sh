#untracked files
git clean -n
git submodule foreach git clean -n
git clean -f
git submodule foreach git clean -f
#untracked dorectories
git clean -n -d
git submodule foreach git clean -n -d
git clean -f -d
git submodule foreach git clean -f -d