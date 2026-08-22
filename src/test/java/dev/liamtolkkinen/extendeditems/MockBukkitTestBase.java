package dev.liamtolkkinen.extendeditems;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockbukkit.mockbukkit.MockBukkit;

abstract class MockBukkitTestBase {
    @BeforeEach
    void setUpMockBukkit() {
        MockBukkit.mock();
    }

    @AfterEach
    void tearDownMockBukkit() {
        MockBukkit.unmock();
    }
}
