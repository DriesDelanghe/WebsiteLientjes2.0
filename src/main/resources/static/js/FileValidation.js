Filevalidation = () => {
    const fi = document.getElementById('formFile');
    const button = document.getElementById('fileUpload');
    const info = document.getElementById('size');
    if(fi.files.length > 0){
        for(let i = 0; i <= fi.files.length - 1; i++){
            let fsize = fi.files.item(i).size;
            let file = Math.round((fsize/1024));

            if(file >= 2048){
                button.disabled = true;
                info.innerHTML = '<span class="text-danger"> File is <b>' + file + '</b> Kb groot, gelieve een file van minder dan ' +
                    '2048Kb te selecteren. Ga naar <a target="_blank" href="https://compressimage.toolur.com/">deze site</a> als je de afbeelding wilt comprimeren</span>'
            }
            else{
                button.disabled = false;
                info.innerHTML = '<b>' + file + '</b> KB';
            }
        }
    }

    const fi1 = document.getElementById('formFile1');
    const button1 = document.getElementById('fileUpload1');
    const info1 = document.getElementById('size1');
    if(fi1.files.length > 0){
        for(let i = 0; i <= fi.files.length - 1; i++){
            let fsize = fi.files.item(i).size;
            let file = Math.round((fsize/1024));

            if(file >= 2048){
                button1.disabled = true;
                info1.innerHTML = '<span class="text-danger"> File is <b>' + file + '</b> Kb groot, gelieve een file van minder dan ' +
                    '2048Kb te selecteren. Ga naar <a target="_blank" href="https://compressimage.toolur.com/">deze site</a> als je de afbeelding wilt comprimeren</span>'
            }
            else{
                button1.disabled = false;
                info1.innerHTML = '<b>' + file + '</b> KB';
            }
        }
    }
}
