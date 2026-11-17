package org.hanayori.liriope.border;

/** One flat panel of the ring: chord midpoint, yaw facing the viewer's side (in or out), chord width, texture phase in [0,1). */
public record Panel(int index, double x, double z, float yaw, double width, double phase) {}
