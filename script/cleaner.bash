#!/bin/bash 
cd BUILD
umount -l squashfs
umount -l mnt
cd ..
rm -r BUILD