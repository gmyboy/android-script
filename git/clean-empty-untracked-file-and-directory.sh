#untracked files or directories
echo "-> Clean all untracked files or directories"
git submodule foreach git clean -n -f -d
git clean -n -f -d