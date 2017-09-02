package com.jepsiandco.game;

import com.badlogic.gdx.math.Vector3;

class ShapesFood {

    // TODO : instead of having each point saved, we save only the corners and two vectors for each point
    // Then we draw a curve between two points we a direction of start that is one vector of each point
    // * See the path tool in GIMP *

    // TODO : store if the shape is closed or not

    static final Vector3[] croissantShape = {
            new Vector3(181, 373, 0),
            new Vector3(187, 348, 0),
            new Vector3(213, 323, 0),
            new Vector3(235, 319, 0),
            new Vector3(257, 321, 0),
            new Vector3(285, 323, 0),
            new Vector3(299, 331, 0),
            new Vector3(312, 344, 0),
            new Vector3(316, 367, 0),
            new Vector3(321, 388, 0),
            new Vector3(322, 396, 0),
            new Vector3(347, 383, 0),
            new Vector3(372, 358, 0),
            new Vector3(390, 329, 0),
            new Vector3(385, 303, 0),
            new Vector3(369, 273, 0),
            new Vector3(351, 248, 0),
            new Vector3(322, 224, 0),
            new Vector3(292, 206, 0),
            new Vector3(262, 189, 0),
            new Vector3(225, 175, 0),
            new Vector3(211, 184, 0),
            new Vector3(192, 197, 0),
            new Vector3(178, 210, 0),
            new Vector3(153, 232, 0),
            new Vector3(143, 247, 0),
            new Vector3(128, 268, 0),
            new Vector3(116, 299, 0),
            new Vector3(112, 332, 0),
            new Vector3(119, 359, 0),
            new Vector3(135, 382, 0),
            new Vector3(149, 396, 0),
            new Vector3(169, 412, 0),
            new Vector3(182, 408, 0),
            new Vector3(182, 387, 0),
            new Vector3(181, 373, 0),
    };

    static final Vector3[] cheeseShape = {
            new Vector3(126, 179, 0),
            new Vector3(121, 239, 0),
            new Vector3(120, 289, 0),
            new Vector3(203, 362, 0),
            new Vector3(275, 423, 0),
            new Vector3(311, 386, 0),
            new Vector3(351, 344, 0),
            new Vector3(354, 285, 0),
            new Vector3(360, 227, 0),
            new Vector3(240, 204, 0),
            new Vector3(126, 179, 0),
    };

    static final Vector3[] pizzaShape = {
            new Vector3(318, 212, 0),
            new Vector3(302, 200, 0),
            new Vector3(283, 191, 0),
            new Vector3(263, 185, 0),
            new Vector3(243, 179, 0),
            new Vector3(222, 178, 0),
            new Vector3(202, 183, 0),
            new Vector3(183, 191, 0),
            new Vector3(164, 200, 0),
            new Vector3(145, 211, 0),
            new Vector3(133, 232, 0),
            new Vector3(124, 252, 0),
            new Vector3(120, 273, 0),
            new Vector3(118, 293, 0),
            new Vector3(119, 313, 0),
            new Vector3(124, 334, 0),
            new Vector3(129, 356, 0),
            new Vector3(136, 376, 0),
            new Vector3(151, 390, 0),
            new Vector3(168, 402, 0),
            new Vector3(186, 413, 0),
            new Vector3(205, 423, 0),
            new Vector3(226, 428, 0),
            new Vector3(247, 429, 0),
            new Vector3(267, 428, 0),
            new Vector3(287, 419, 0),
            new Vector3(307, 411, 0),
            new Vector3(325, 402, 0),
            new Vector3(334, 383, 0),
            new Vector3(344, 365, 0),
            new Vector3(356, 348, 0),
            new Vector3(362, 329, 0),
            new Vector3(363, 309, 0),
            new Vector3(356, 290, 0),
            new Vector3(348, 271, 0),
            new Vector3(339, 253, 0),
            new Vector3(335, 233, 0),
            new Vector3(318, 212, 0),
    };

    static final Vector3[] cheeseburgerShape = {
            new Vector3(131, 228, 0),
            new Vector3(125, 248, 0),
            new Vector3(124, 270, 0),
            new Vector3(125, 294, 0),
            new Vector3(129, 314, 0),
            new Vector3(131, 334, 0),
            new Vector3(135, 355, 0),
            new Vector3(142, 373, 0),
            new Vector3(152, 393, 0),
            new Vector3(164, 409, 0),
            new Vector3(179, 423, 0),
            new Vector3(200, 431, 0),
            new Vector3(223, 434, 0),
            new Vector3(245, 435, 0),
            new Vector3(266, 433, 0),
            new Vector3(286, 429, 0),
            new Vector3(304, 419, 0),
            new Vector3(320, 403, 0),
            new Vector3(329, 383, 0),
            new Vector3(334, 362, 0),
            new Vector3(336, 341, 0),
            new Vector3(338, 319, 0),
            new Vector3(339, 299, 0),
            new Vector3(339, 278, 0),
            new Vector3(336, 257, 0),
            new Vector3(334, 237, 0),
            new Vector3(332, 217, 0),
            new Vector3(318, 201, 0),
            new Vector3(298, 202, 0),
            new Vector3(278, 202, 0),
            new Vector3(257, 201, 0),
            new Vector3(237, 201, 0),
            new Vector3(216, 202, 0),
            new Vector3(195, 202, 0),
            new Vector3(175, 203, 0),
            new Vector3(152, 202, 0),
            new Vector3(132, 203, 0),
            new Vector3(131, 228, 0),
    };

    static final Vector3[] watermelonShape = {
            new Vector3(138, 373, 0),
            new Vector3(158, 377, 0),
            new Vector3(180, 378, 0),
            new Vector3(202, 376, 0),
            new Vector3(222, 376, 0),
            new Vector3(243, 376, 0),
            new Vector3(264, 376, 0),
            new Vector3(284, 373, 0),
            new Vector3(304, 373, 0),
            new Vector3(324, 373, 0),
            new Vector3(344, 373, 0),
            new Vector3(364, 370, 0),
            new Vector3(363, 350, 0),
            new Vector3(367, 330, 0),
            new Vector3(367, 309, 0),
            new Vector3(367, 289, 0),
            new Vector3(365, 269, 0),
            new Vector3(356, 250, 0),
            new Vector3(346, 231, 0),
            new Vector3(330, 216, 0),
            new Vector3(312, 207, 0),
            new Vector3(293, 199, 0),
            new Vector3(274, 192, 0),
            new Vector3(251, 190, 0),
            new Vector3(231, 189, 0),
            new Vector3(211, 196, 0),
            new Vector3(190, 201, 0),
            new Vector3(169, 209, 0),
            new Vector3(153, 221, 0),
            new Vector3(139, 236, 0),
            new Vector3(128, 254, 0),
            new Vector3(118, 272, 0),
            new Vector3(114, 292, 0),
            new Vector3(111, 312, 0),
            new Vector3(110, 332, 0),
            new Vector3(110, 352, 0),
            new Vector3(118, 371, 0),
            new Vector3(138, 373, 0),
    };

    static final Vector3[] frenchstickShape = {
            new Vector3(251, 243, 0),
            new Vector3(229, 237, 0),
            new Vector3(210, 226, 0),
            new Vector3(191, 217, 0),
            new Vector3(171, 212, 0),
            new Vector3(151, 206, 0),
            new Vector3(131, 208, 0),
            new Vector3(112, 215, 0),
            new Vector3(99, 233, 0),
            new Vector3(98, 255, 0),
            new Vector3(109, 272, 0),
            new Vector3(123, 287, 0),
            new Vector3(135, 303, 0),
            new Vector3(148, 321, 0),
            new Vector3(162, 336, 0),
            new Vector3(179, 348, 0),
            new Vector3(197, 357, 0),
            new Vector3(217, 364, 0),
            new Vector3(237, 371, 0),
            new Vector3(259, 377, 0),
            new Vector3(280, 382, 0),
            new Vector3(302, 390, 0),
            new Vector3(322, 396, 0),
            new Vector3(344, 396, 0),
            new Vector3(364, 396, 0),
            new Vector3(381, 385, 0),
            new Vector3(381, 365, 0),
            new Vector3(370, 348, 0),
            new Vector3(356, 332, 0),
            new Vector3(344, 315, 0),
            new Vector3(329, 299, 0),
            new Vector3(313, 284, 0),
            new Vector3(294, 273, 0),
            new Vector3(278, 261, 0),
            new Vector3(251, 243, 0),
    };
}
