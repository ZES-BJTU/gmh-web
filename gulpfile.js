var gulp        = require('gulp');
var browserSync = require('browser-sync').create();
var less        = require('gulp-less');
var reload      = browserSync.reload;

// 静态服务器 + 监听 less/html 文件
gulp.task('serve', ['less'], function() {

    // browserSync.init({
    //     server: "./src/main/webapp/gmh"
    // });

    gulp.watch("src/main/webapp/gmh/less/*.less", ['less']);
    gulp.watch("src/main/webapp/gmh/*.html").on('change', reload);
});

// less编译后的css将注入到浏览器里实现更新
gulp.task('less', function() {
    return gulp.src("src/main/webapp/gmh/less/*.less")
        .pipe(less())
        .pipe(gulp.dest("src/main/webapp/gmh/css"))
        .pipe(reload({stream: true}));
});

gulp.task('default', ['serve']);