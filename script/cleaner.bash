#!/bin/bash
echo hello
pwd
cd BUILD
umount squashfs
umount mnt
cd ..
rm -r BUILD
