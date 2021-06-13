#!/bin/bash

export YARN_LOCAL_DIRECTORY=/home/dtim/data/nm-local-dir
export YARN_LOCAL_DISK_LOOP_DEVICE=/home/dtim/local_disk_loop_devices/yarn_local_disk_loop_device

mount -o loop $YARN_LOCAL_DISK_LOOP_DEVICE $YARN_LOCAL_DIRECTORY


