#include "bridge_helper.h"

/**
 * Compress an uint64 into an uint32 with minimum data lost
 *
 * The result will be 3bits of exponent (e) and 29bits of fraction (f).
 *
 * The original value can be recovered using f << (10 * e)
 */
uint64_t down_scale_traffic(uint64_t value) {
    uint64_t e = 0;
    while (value > 0x1FFFFFFFu) {
        e++;
        value >>= 10;
    }

    return value | (e << 29);
}

