const FileUtil = {

    uploadFile: (fileForm, url) => {
        if (!url instanceof URL)
            return false;

        return fetch(url, {
            method: 'POST',
            body: fileForm
        })
    },

    uploadImage: (e) => {
        let file = e.files[0]
        if (!FileUtil.validateImageSize(FileUtil.getImageSize(file))) {
            Common.alert('이미지 사이즈를 확인해주십시요', 'red');
            return false;
        }

        return FileUtil.getImagePath(file);

    },

    getImagePath: (file) => {
        let formData = new FormData;
        let uploadUrl = new URL(location.origin + '/s3/resource');
        formData.append('file', file);

        return FileUtil.uploadFile(formData, uploadUrl)
            .then((rs) => rs.json())
            .then((json) => {
                if (json.code && json.code !== '0000') {
                    Common.alert('이미지 업로드에 실패했습니다.', 'red');
                    return false;
                } else {
                    if (json.result) {
                        return json.result.path;
                    }
                }
            });
    },

    validateImageSize: (size) => {

        return true;
    },

    validateImageFileType: (type) => {

    },

    getImageSize: (file) => {
        let data = {};
        let objectUrl = URL.createObjectURL(file);
        let img = new Image();
        img.addEventListener('load', function (data) {
            data.height = this.height;
            data.width = this.width;

            URL.revokeObjectURL(objectUrl);
        }.bind(img, data));
        img.src = objectUrl;
        return data;
    },

    getFileSize: (byte) => {

        if (byte < 1024) {
            return `${byte} bytes`;
        } else if (byte >= 1024 && byte < 1048576) {
            return `${(byte / 1024).toFixed(1)} KB`;
        } else if (byte >= 1048576) {
            return `${(byte / 1048576).toFixed(1)} MB`;
        }
    }
}