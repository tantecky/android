#ifdef GL_ES
precision highp float;
#endif

#define FACTOR 10.0
#define RADIUS 0.1
#define PI 3.1415926538

varying vec2 v_center;

uniform vec2 u_touch;
uniform sampler2D u_quantity;

void main(){
    float x = v_center.x;
    float y = v_center.y;

    vec2 dist = vec2(x - u_touch.x, y - u_touch.y);
    float angle = atan(dist.y, dist.x) + PI;

    float ux = texture2D(u_quantity, v_center).r + sin(angle);
    float uy = texture2D(u_quantity, v_center).g + cos(angle);

    gl_FragColor = vec4(ux, uy, 0.0, 0.0);
}
