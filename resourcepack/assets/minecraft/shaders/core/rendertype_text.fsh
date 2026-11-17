#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;

flat in int alliumMarker;
in vec2 localUV;
in vec3 viewPos;
in vec4 rawColor;

out vec4 fragColor;

void main() {
    if (alliumMarker != 0) {
        // Force-field panel, as vanilla's world border: one 16x16 tile per 2 blocks anchored to world Y
        // (panel bottom is even), u and v both offset by a 3 s scroll, tinted, faded by (1 - d/D)^4.
        vec2 uvSize = 16.0 / vec2(textureSize(Sampler0, 0));
        vec2 uvMin = texCoord0 - localUV * uvSize;
        vec3 dPdx = dFdx(viewPos), dPdy = dFdy(viewPos);
        vec2 dUVdx = dFdx(localUV), dUVdy = dFdy(localUV);
        float det = dUVdx.x * dUVdy.y - dUVdx.y * dUVdy.x;
        float width  = length((dPdx * dUVdy.y - dPdy * dUVdx.y) / det);
        float height = length((dPdy * dUVdx.x - dPdx * dUVdy.x) / det);
        ivec3 c = ivec3(round(rawColor.rgb * 255.0));
        float phase = float((c.r & 3) << 4 | (c.g & 3) << 2 | (c.b & 3)) / 64.0;
        float scroll = fract(GameTime * 400.0);
        vec2 tile = fract(vec2(phase + localUV.x * width * 0.5 + scroll, (localUV.y - 1.0) * height * 0.5 + scroll));
        vec4 tex = texture(Sampler0, uvMin + tile * uvSize);
        if (tex.a == 0.0) discard;
        // vanilla: alpha = (1 - distanceToBorder / renderDistance)^4, uniform over a wall. Distance to this
        // panel's plane stands in for distance to the border; FogRenderDistanceEnd is the render distance in blocks.
        vec3 n = normalize(cross(dPdx, dPdy));
        float fade = pow(clamp(1.0 - abs(dot(viewPos, n)) / FogRenderDistanceEnd, 0.0, 1.0), 4.0);
        fragColor = vec4(tex.rgb * vec3(c & ~3) / 255.0, tex.a * fade);
        return;
    }

    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }
    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
