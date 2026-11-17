#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec4 Color;
in ivec2 UV2;

uniform sampler2D Sampler2;

out float sphericalVertexDistance;
out float cylindricalVertexDistance;
out vec4 vertexColor;

// Allium border: background alpha 62 is the pack-less fallback wall; hide it (63 is vanilla name tags).
flat out int alliumMarker;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    sphericalVertexDistance = fog_spherical_distance(Position);
    cylindricalVertexDistance = fog_cylindrical_distance(Position);
    alliumMarker = abs(Color.a * 255.0 - 62.0) < 0.5 ? 1 : 0;
    vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);
}
