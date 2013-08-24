#!/bin/bash

# indique au système que l'argument qui suit est le programme utilisé pour exécuter ce fichier.
# En cas général les "#" servent à faire des commentaires comme ici

if (($#==5))
then
  pidginPath="$1"
  jitsiPath="$2"
  isoPath="$3"
  linuxUser="$4"
  desPath="$5"
else
  echo "error arguments" 1>&2
# exit
fi


echo "Now i am root"
id
echo "yes!"

echo "You are in "
pwd

mkdir -p BUILD
#cp -v ../tails-i386-0.18.iso BUILD
cp -v $isoPath BUILD
cd BUILD/
echo "You are in "
pwd

mkdir mnt
mount -o loop tails-i386-0.18.iso mnt/
mkdir extract-cd
rsync --exclude=/live/filesystem.squashfs -a mnt/ extract-cd
mkdir squashfs
mount -t squashfs -o loop mnt/live/filesystem.squashfs squashfs
mkdir edit
cp -a squashfs/* edit/ 2> hello

cd ..
echo "You are in "
pwd

mkdir ./BUILD/edit/home/$linuxUser
mkdir ./BUILD/edit/home/$linuxUser/.purple ./BUILD/edit/home/$linuxUser/.jitsi
cp $pidginPath/* ./BUILD/edit/home/$linuxUser/.purple/
cp $jitsiPath/* ./BUILD/edit/home/$linuxUser/.jitsi/

echo "Making squashfs..."
mksquashfs ./BUILD/edit ./BUILD/extract-cd/live/filesystem.squashfs

cd ./BUILD/extract-cd
echo "You are in "
pwd

echo "Building iso file ..."
echo $desPath/monimage.iso
genisoimage -o $desPath/monimage.iso -r -J -no-emul-boot -boot-load-size 4 -boot-info-table -b isolinux/isolinux.bin -c isolinux/boot.cat ./
#ls -la
 
#exit 0
