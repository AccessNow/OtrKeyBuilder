#!/bin/bash 
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

mkdir -p BUILD
cd BUILD/
mkdir mnt
mount -o loop ../$isoPath mnt/
mkdir extract-cd
rsync --exclude=/live/filesystem.squashfs -a mnt/ extract-cd
mkdir squashfs
mount -t squashfs -o loop mnt/live/filesystem.squashfs squashfs
mkdir edit
cp -a squashfs/* edit/ 2> hello

cd ..
mkdir ./BUILD/edit/home/$linuxUser
mkdir ./BUILD/edit/home/$linuxUser/.purple ./BUILD/edit/home/$linuxUser/.jitsi
cp $pidginPath/* ./BUILD/edit/home/$linuxUser/.purple/
cp $jitsiPath/* ./BUILD/edit/home/$linuxUser/.jitsi/
mksquashfs ./BUILD/edit ./BUILD/extract-cd/live/filesystem.squashfs
cd ./BUILD/extract-cd
genisoimage -o ../../$desPath/monimage.iso -r -J -no-emul-boot -boot-load-size 4 -boot-info-table -b isolinux/isolinux.bin -c isolinux/boot.cat ./