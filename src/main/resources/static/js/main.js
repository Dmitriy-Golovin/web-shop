window.onload = function() {
    document.querySelector('.menu_burger').onclick = function() {
        let active = Number(this.dataset.active),
            menu = this.parentElement.querySelector('.nav_menu');

        if (active === 0) {
            this.dataset.active = 1;
            menu.dataset.active = 1;
        } else {
            this.dataset.active = 0;
            menu.dataset.active = 0;
        }
    }

    if (document.querySelector('.field_file')) {
        document.querySelector('.field_file').onchange = function() {
            this.closest('form').submit();
        }
    }

    document.querySelectorAll('.thumb_img').forEach(function(el) {
        el.onclick = function() {
            let mainImg = this.closest('.info_box_top').querySelector('.zoom_img');
            mainImg.src = this.src;
        }
    });

    document.querySelectorAll('.zoom_img').forEach(function(el) {
        el.onclick = function() {
            let modal = document.querySelector('#large_image'),
                imgContainer = modal.querySelector('img');
    
            imgContainer.setAttribute('src', this.src);
    
            modal.onclick = () => {
                modal.classList.remove('modal_active');
                modal.dataset.active = 0;
            }
    
            modal.classList.add('modal_active');
            modal.dataset.active = 1;
        }
    });

    document.querySelectorAll('.show_order_history').forEach(function(el) {
        el.onclick = function(e) {
            let modal = this.parentElement.querySelector('.modal');

            modal.onclick = () => {
                modal.classList.remove('modal_active');
                modal.dataset.active = 0;
            }
    
            modal.classList.add('modal_active');
            modal.dataset.active = 1;
            
            e.preventDefault();
        }
    });
}