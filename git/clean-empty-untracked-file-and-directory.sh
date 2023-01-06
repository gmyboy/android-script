#untracked files
echo "-> Clean all untracked files"
git submodule foreach git clean -f
git clean -f

echo ""
#untracked directories
echo "-> Clean all untracked directories"
git submodule foreach git clean -f -d
git clean -f -d