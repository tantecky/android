#ifdef GL_ES
precision highp float;
#endif

#define FACTOR 10.0
#define RADIUS 0.05

varying vec2 v_center;

uniform vec2 u_touch;
uniform sampler2D u_quantity;

void main(){
    float x = v_center.x;
    float y = v_center.y;

    float splash = smoothstep(u_touch.x - RADIUS, u_touch.x + RADIUS, x)
                 * smoothstep(u_touch.x + RADIUS, u_touch.x - RADIUS, x)
                 * smoothstep(u_touch.y - RADIUS, u_touch.y + RADIUS, y)
                 * smoothstep(u_touch.y + RADIUS, u_touch.y - RADIUS, y);
    splash *= FACTOR;

    float fx = texture2D(u_quantity, v_center).r;
    gl_FragColor = vec4(fx + splash, 0.0, 0.0, 0.0);
}
