git clone https://github.com/ildoonet/tf-pose-estimation
pip install numpy
pip install Cython
cd tf-openpose
pip install -r requirements.txt
pip install matplotlib
brew install swig
pip install tensorflow
pip install opencv-python==3.4.0.14
cd models/graph/cmu
bash download.sh
cd ../../..
cd tf_pose/pafprocess/

