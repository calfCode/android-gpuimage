/*
 * Copyright (C) 2018 CyberAgent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.cyberagent.android.gpuimage.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.Matrix;

import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.filter.*;
import jp.co.cyberagent.android.gpuimage.sample.R;

public final class GPUImageFilterTools2 {

    public interface OnGpuImageFilterChosenListener {
        void onGpuImageFilterChosenListener(GPUImageFilter filter);
    }

    public static void showDialog(Context context, OnGpuImageFilterChosenListener listener) {
        FilterList filters = new FilterList();
        filters.addFilter("Contrast", FilterType.CONTRAST);
        filters.addFilter("Invert", FilterType.INVERT);
        filters.addFilter("Pixelation", FilterType.PIXELATION);
        filters.addFilter("Hue", FilterType.HUE);
        filters.addFilter("Gamma", FilterType.GAMMA);
        filters.addFilter("Brightness", FilterType.BRIGHTNESS);
        filters.addFilter("Sepia", FilterType.SEPIA);
        filters.addFilter("Grayscale", FilterType.GRAYSCALE);
        filters.addFilter("Sharpness", FilterType.SHARPEN);
        filters.addFilter("Sobel Edge Detection", FilterType.SOBEL_EDGE_DETECTION);
        filters.addFilter("Threshold Edge Detection", FilterType.THRESHOLD_EDGE_DETECTION);
        filters.addFilter("3x3 Convolution", FilterType.THREE_X_THREE_CONVOLUTION);
        filters.addFilter("Emboss", FilterType.EMBOSS);
        filters.addFilter("Posterize", FilterType.POSTERIZE);
        filters.addFilter("Grouped filters", FilterType.FILTER_GROUP);
        filters.addFilter("Saturation", FilterType.SATURATION);
        filters.addFilter("Exposure", FilterType.EXPOSURE);
        filters.addFilter("Highlight Shadow", FilterType.HIGHLIGHT_SHADOW);
        filters.addFilter("Monochrome", FilterType.MONOCHROME);
        filters.addFilter("Opacity", FilterType.OPACITY);
        filters.addFilter("RGB", FilterType.RGB);
        filters.addFilter("White Balance", FilterType.WHITE_BALANCE);
        filters.addFilter("Vignette", FilterType.VIGNETTE);
        filters.addFilter("ToneCurve", FilterType.TONE_CURVE);
        filters.addFilter("Luminance", FilterType.LUMINANCE);
        filters.addFilter("Luminance Threshold", FilterType.LUMINANCE_THRESHSOLD);
        filters.addFilter("Blend (Difference)", FilterType.BLEND_DIFFERENCE);
        filters.addFilter("Blend (Source Over)", FilterType.BLEND_SOURCE_OVER);
        filters.addFilter("Blend (Color Burn)", FilterType.BLEND_COLOR_BURN);
        filters.addFilter("Blend (Color Dodge)", FilterType.BLEND_COLOR_DODGE);
        filters.addFilter("Blend (Darken)", FilterType.BLEND_DARKEN);
        filters.addFilter("Blend (Dissolve)", FilterType.BLEND_DISSOLVE);
        filters.addFilter("Blend (Exclusion)", FilterType.BLEND_EXCLUSION);
        filters.addFilter("Blend (Hard Light)", FilterType.BLEND_HARD_LIGHT);
        filters.addFilter("Blend (Lighten)", FilterType.BLEND_LIGHTEN);
        filters.addFilter("Blend (Add)", FilterType.BLEND_ADD);
        filters.addFilter("Blend (Divide)", FilterType.BLEND_DIVIDE);
        filters.addFilter("Blend (Multiply)", FilterType.BLEND_MULTIPLY);
        filters.addFilter("Blend (Overlay)", FilterType.BLEND_OVERLAY);
        filters.addFilter("Blend (Screen)", FilterType.BLEND_SCREEN);
        filters.addFilter("Blend (Alpha)", FilterType.BLEND_ALPHA);
        filters.addFilter("Blend (Color)", FilterType.BLEND_COLOR);
        filters.addFilter("Blend (Hue)", FilterType.BLEND_HUE);
        filters.addFilter("Blend (Saturation)", FilterType.BLEND_SATURATION);
        filters.addFilter("Blend (Luminosity)", FilterType.BLEND_LUMINOSITY);
        filters.addFilter("Blend (Linear Burn)", FilterType.BLEND_LINEAR_BURN);
        filters.addFilter("Blend (Soft Light)", FilterType.BLEND_SOFT_LIGHT);
        filters.addFilter("Blend (Subtract)", FilterType.BLEND_SUBTRACT);
        filters.addFilter("Blend (Chroma Key)", FilterType.BLEND_CHROMA_KEY);
        filters.addFilter("Blend (Normal)", FilterType.BLEND_NORMAL);
        filters.addFilter("Lookup (Amatorka)", FilterType.LOOKUP_AMATORKA);
        filters.addFilter("Gaussian Blur", FilterType.GAUSSIAN_BLUR);
        filters.addFilter("Crosshatch", FilterType.CROSSHATCH);
        filters.addFilter("Box Blur", FilterType.BOX_BLUR);
        filters.addFilter("CGA Color Space", FilterType.CGA_COLORSPACE);
        filters.addFilter("Dilation", FilterType.DILATION);
        filters.addFilter("Kuwahara", FilterType.KUWAHARA);
        filters.addFilter("RGB Dilation", FilterType.RGB_DILATION);
        filters.addFilter("Sketch", FilterType.SKETCH);
        filters.addFilter("Toon", FilterType.TOON);
        filters.addFilter("Smooth Toon", FilterType.SMOOTH_TOON);
        filters.addFilter("Bulge Distortion", FilterType.BULGE_DISTORTION);
        filters.addFilter("Glass Sphere", FilterType.GLASS_SPHERE);
        filters.addFilter("Haze", FilterType.HAZE);
        filters.addFilter("Laplacian", FilterType.LAPLACIAN);
        filters.addFilter("Non Maximum Suppression", FilterType.NON_MAXIMUM_SUPPRESSION);
        filters.addFilter("Sphere Refraction", FilterType.SPHERE_REFRACTION);
        filters.addFilter("Swirl", FilterType.SWIRL);
        filters.addFilter("Weak Pixel Inclusion", FilterType.WEAK_PIXEL_INCLUSION);
        filters.addFilter("False Color", FilterType.FALSE_COLOR);
        filters.addFilter("Color Balance", FilterType.COLOR_BALANCE);
        filters.addFilter("Levels Min (Mid Adjust)", FilterType.LEVELS_FILTER_MIN);
        filters.addFilter("Bilateral Blur", FilterType.BILATERAL_BLUR);
        filters.addFilter("Zoom Blur", FilterType.ZOOM_BLUR);
        filters.addFilter("Transform (2-D)", FilterType.TRANSFORM2D);
        filters.addFilter("Solarize", FilterType.SOLARIZE);
        filters.addFilter("Vibrance", FilterType.VIBRANCE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose a filter");
        builder.setItems(filters.names.toArray(new String[0]), (dialog, which) -> {
            GPUImageFilter filter = createFilterForType(context, filters.filters.get(which));
            listener.onGpuImageFilterChosenListener(filter);
        });
        builder.create().show();
    }

    /* ========== 根据 FilterType 创建对应过滤器 ========== */
    public static GPUImageFilter createFilterForType(Context context, FilterType type) {
        switch (type) {
            case CONTRAST: return new GPUImageContrastFilter(2.0f);
            case GAMMA: return new GPUImageGammaFilter(2.0f);
            case INVERT: return new GPUImageColorInvertFilter();
            case PIXELATION: return new GPUImagePixelationFilter();
            case HUE: return new GPUImageHueFilter(90.0f);
            case BRIGHTNESS: return new GPUImageBrightnessFilter(1.5f);
            case GRAYSCALE: return new GPUImageGrayscaleFilter();
            case SEPIA: return new GPUImageSepiaToneFilter();
            case SHARPEN: return new GPUImageSharpenFilter();
            case SOBEL_EDGE_DETECTION: return new GPUImageSobelEdgeDetectionFilter();
            case THRESHOLD_EDGE_DETECTION: return new GPUImageThresholdEdgeDetectionFilter();
            case THREE_X_THREE_CONVOLUTION: return new GPUImage3x3ConvolutionFilter();
            case EMBOSS: return new GPUImageEmbossFilter();
            case POSTERIZE: return new GPUImagePosterizeFilter();
            case FILTER_GROUP: {
                List<GPUImageFilter> list = new LinkedList<>();
                list.add(new GPUImageContrastFilter());
                list.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
                list.add(new GPUImageGrayscaleFilter());
                return new GPUImageFilterGroup(list);
            }
            case SATURATION: return new GPUImageSaturationFilter(1.0f);
            case EXPOSURE: return new GPUImageExposureFilter(0.0f);
            case HIGHLIGHT_SHADOW: return new GPUImageHighlightShadowFilter(0.0f, 1.0f);
            case MONOCHROME: return new GPUImageMonochromeFilter(1.0f,
                    new float[]{0.6f, 0.45f, 0.3f, 1.0f});
            case OPACITY: return new GPUImageOpacityFilter(1.0f);
            case RGB: return new GPUImageRGBFilter(1.0f, 1.0f, 1.0f);
            case WHITE_BALANCE: return new GPUImageWhiteBalanceFilter(5000.0f, 0.0f);
            case VIGNETTE: return new GPUImageVignetteFilter(new PointF(0.5f, 0.5f),
                    new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
            case TONE_CURVE: {
                GPUImageToneCurveFilter f = new GPUImageToneCurveFilter();
                try {
                    f.setFromCurveFileInputStream(context.getResources()
                            .openRawResource(R.raw.tone_cuver_sample));
                } catch (Exception ignore) {}
                return f;
            }
            case LUMINANCE: return new GPUImageLuminanceFilter();
            case LUMINANCE_THRESHSOLD: return new GPUImageLuminanceThresholdFilter(0.5f);

            /* Blend Filters */
            case BLEND_DIFFERENCE: return createBlendFilter(context, GPUImageDifferenceBlendFilter.class);
            case BLEND_SOURCE_OVER: return createBlendFilter(context, GPUImageSourceOverBlendFilter.class);
            case BLEND_COLOR_BURN: return createBlendFilter(context, GPUImageColorBurnBlendFilter.class);
            case BLEND_COLOR_DODGE: return createBlendFilter(context, GPUImageColorDodgeBlendFilter.class);
            case BLEND_DARKEN: return createBlendFilter(context, GPUImageDarkenBlendFilter.class);
            case BLEND_DISSOLVE: return createBlendFilter(context, GPUImageDissolveBlendFilter.class);
            case BLEND_EXCLUSION: return createBlendFilter(context, GPUImageExclusionBlendFilter.class);
            case BLEND_HARD_LIGHT: return createBlendFilter(context, GPUImageHardLightBlendFilter.class);
            case BLEND_LIGHTEN: return createBlendFilter(context, GPUImageLightenBlendFilter.class);
            case BLEND_ADD: return createBlendFilter(context, GPUImageAddBlendFilter.class);
            case BLEND_DIVIDE: return createBlendFilter(context, GPUImageDivideBlendFilter.class);
            case BLEND_MULTIPLY: return createBlendFilter(context, GPUImageMultiplyBlendFilter.class);
            case BLEND_OVERLAY: return createBlendFilter(context, GPUImageOverlayBlendFilter.class);
            case BLEND_SCREEN: return createBlendFilter(context, GPUImageScreenBlendFilter.class);
            case BLEND_ALPHA: return createBlendFilter(context, GPUImageAlphaBlendFilter.class);
            case BLEND_COLOR: return createBlendFilter(context, GPUImageColorBlendFilter.class);
            case BLEND_HUE: return createBlendFilter(context, GPUImageHueBlendFilter.class);
            case BLEND_SATURATION: return createBlendFilter(context, GPUImageSaturationBlendFilter.class);
            case BLEND_LUMINOSITY: return createBlendFilter(context, GPUImageLuminosityBlendFilter.class);
            case BLEND_LINEAR_BURN: return createBlendFilter(context, GPUImageLinearBurnBlendFilter.class);
            case BLEND_SOFT_LIGHT: return createBlendFilter(context, GPUImageSoftLightBlendFilter.class);
            case BLEND_SUBTRACT: return createBlendFilter(context, GPUImageSubtractBlendFilter.class);
            case BLEND_CHROMA_KEY: return createBlendFilter(context, GPUImageChromaKeyBlendFilter.class);
            case BLEND_NORMAL: return createBlendFilter(context, GPUImageNormalBlendFilter.class);

            /* Lookup & Effects */
            case LOOKUP_AMATORKA: {
                GPUImageLookupFilter f = new GPUImageLookupFilter();
                f.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.lookup_amatorka));
                return f;
            }
            case GAUSSIAN_BLUR: return new GPUImageGaussianBlurFilter();
            case CROSSHATCH: return new GPUImageCrosshatchFilter();
            case BOX_BLUR: return new GPUImageBoxBlurFilter();
            case CGA_COLORSPACE: return new GPUImageCGAColorspaceFilter();
            case DILATION: return new GPUImageDilationFilter();
            case KUWAHARA: return new GPUImageKuwaharaFilter();
            case RGB_DILATION: return new GPUImageRGBDilationFilter();
            case SKETCH: return new GPUImageSketchFilter();
            case TOON: return new GPUImageToonFilter();
            case SMOOTH_TOON: return new GPUImageSmoothToonFilter();
            case BULGE_DISTORTION: return new GPUImageBulgeDistortionFilter();
            case GLASS_SPHERE: return new GPUImageGlassSphereFilter();
            case HAZE: return new GPUImageHazeFilter();
            case LAPLACIAN: return new GPUImageLaplacianFilter();
            case NON_MAXIMUM_SUPPRESSION: return new GPUImageNonMaximumSuppressionFilter();
            case SPHERE_REFRACTION: return new GPUImageSphereRefractionFilter();
            case SWIRL: return new GPUImageSwirlFilter();
            case WEAK_PIXEL_INCLUSION: return new GPUImageWeakPixelInclusionFilter();
            case FALSE_COLOR: return new GPUImageFalseColorFilter();
            case COLOR_BALANCE: return new GPUImageColorBalanceFilter();
            case LEVELS_FILTER_MIN: return new GPUImageLevelsFilter();
            case BILATERAL_BLUR: return new GPUImageBilateralBlurFilter();
            case ZOOM_BLUR: return new GPUImageZoomBlurFilter();
            case HALFTONE: return new GPUImageHalftoneFilter();
            case TRANSFORM2D: return new GPUImageTransformFilter();
            case SOLARIZE: return new GPUImageSolarizeFilter();
            case VIBRANCE: return new GPUImageVibranceFilter();
            default: return new GPUImageFilter();
        }
    }

    private static GPUImageFilter createBlendFilter(Context context, Class<? extends GPUImageTwoInputFilter> filterClass) {
        try {
            GPUImageTwoInputFilter filter = filterClass.newInstance();
            filter.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return new GPUImageFilter();
        }
    }

    /* ========== 枚举定义 ========== */
    public enum FilterType {
        CONTRAST, GRAYSCALE, SHARPEN, SEPIA, SOBEL_EDGE_DETECTION,
        THRESHOLD_EDGE_DETECTION, THREE_X_THREE_CONVOLUTION, FILTER_GROUP, EMBOSS,
        POSTERIZE, GAMMA, BRIGHTNESS, INVERT, HUE, PIXELATION, SATURATION, EXPOSURE,
        HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE,
        TONE_CURVE, LUMINANCE, LUMINANCE_THRESHSOLD, BLEND_COLOR_BURN,
        BLEND_COLOR_DODGE, BLEND_DARKEN, BLEND_DIFFERENCE, BLEND_DISSOLVE,
        BLEND_EXCLUSION, BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN,
        BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, BLEND_SCREEN,
        BLEND_ALPHA, BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY,
        BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY,
        BLEND_NORMAL, LOOKUP_AMATORKA, GAUSSIAN_BLUR, CROSSHATCH, BOX_BLUR,
        CGA_COLORSPACE, DILATION, KUWAHARA, RGB_DILATION, SKETCH, TOON,
        SMOOTH_TOON, BULGE_DISTORTION, GLASS_SPHERE, HAZE, LAPLACIAN,
        NON_MAXIMUM_SUPPRESSION, SPHERE_REFRACTION, SWIRL, WEAK_PIXEL_INCLUSION,
        FALSE_COLOR, COLOR_BALANCE, LEVELS_FILTER_MIN, BILATERAL_BLUR, ZOOM_BLUR,
        HALFTONE, TRANSFORM2D, SOLARIZE, VIBRANCE
    }

    /* ========== FilterList ========== */
    public static class FilterList {
        public final List<String> names = new LinkedList<>();
        public final List<FilterType> filters = new LinkedList<>();

        public void addFilter(String name, FilterType filter) {
            names.add(name);
            filters.add(filter);
        }
    }

    /* ========== FilterAdjuster ========== */
    public static class FilterAdjuster {
        private final Adjuster adjuster;

        public FilterAdjuster(GPUImageFilter filter) {
            adjuster = createAdjuster(filter);
        }

        public boolean canAdjust() {
            return adjuster != null;
        }

        public void adjust(int percentage) {
            if (adjuster != null) adjuster.adjust(percentage);
        }

        private static abstract class Adjuster<T extends GPUImageFilter> {
            protected final T filter;
            protected Adjuster(T filter) { this.filter = filter; }
            public abstract void adjust(int percentage);

            protected float range(int percentage, float start, float end) {
                return (end - start) * percentage / 100f + start;
            }
            protected int range(int percentage, int start, int end) {
                return (end - start) * percentage / 100 + start;
            }
        }

        /* ——— 各种 Adjuster 实现 ——— */
        private static Adjuster<?> createAdjuster(GPUImageFilter filter) {
            if (filter instanceof GPUImageSharpenFilter) return new SharpnessAdjuster((GPUImageSharpenFilter) filter);
            if (filter instanceof GPUImageSepiaToneFilter) return new SepiaAdjuster((GPUImageSepiaToneFilter) filter);
            if (filter instanceof GPUImageContrastFilter) return new ContrastAdjuster((GPUImageContrastFilter) filter);
            if (filter instanceof GPUImageGammaFilter) return new GammaAdjuster((GPUImageGammaFilter) filter);
            if (filter instanceof GPUImageBrightnessFilter) return new BrightnessAdjuster((GPUImageBrightnessFilter) filter);
            if (filter instanceof GPUImageSobelEdgeDetectionFilter) return new SobelAdjuster((GPUImageSobelEdgeDetectionFilter) filter);
            if (filter instanceof GPUImageThresholdEdgeDetectionFilter) return new ThresholdAdjuster((GPUImageThresholdEdgeDetectionFilter) filter);
            if (filter instanceof GPUImage3x3ConvolutionFilter) return new ThreeXThreeConvolutionAdjuster((GPUImage3x3ConvolutionFilter) filter);
            if (filter instanceof GPUImageEmbossFilter) return new EmbossAdjuster((GPUImageEmbossFilter) filter);
            if (filter instanceof GPUImagePosterizeFilter) return new PosterizeAdjuster((GPUImagePosterizeFilter) filter);
            if (filter instanceof GPUImageHueFilter) return new HueAdjuster((GPUImageHueFilter) filter);
            if (filter instanceof GPUImagePixelationFilter) return new PixelationAdjuster((GPUImagePixelationFilter) filter);
            if (filter instanceof GPUImageSaturationFilter) return new SaturationAdjuster((GPUImageSaturationFilter) filter);
            if (filter instanceof GPUImageExposureFilter) return new ExposureAdjuster((GPUImageExposureFilter) filter);
            if (filter instanceof GPUImageHighlightShadowFilter) return new HighlightShadowAdjuster((GPUImageHighlightShadowFilter) filter);
            if (filter instanceof GPUImageMonochromeFilter) return new MonochromeAdjuster((GPUImageMonochromeFilter) filter);
            if (filter instanceof GPUImageOpacityFilter) return new OpacityAdjuster((GPUImageOpacityFilter) filter);
            if (filter instanceof GPUImageRGBFilter) return new RGBAdjuster((GPUImageRGBFilter) filter);
            if (filter instanceof GPUImageWhiteBalanceFilter) return new WhiteBalanceAdjuster((GPUImageWhiteBalanceFilter) filter);
            if (filter instanceof GPUImageVignetteFilter) return new VignetteAdjuster((GPUImageVignetteFilter) filter);
            if (filter instanceof GPUImageLuminanceThresholdFilter) return new LuminanceThresholdAdjuster((GPUImageLuminanceThresholdFilter) filter);
            if (filter instanceof GPUImageDissolveBlendFilter) return new DissolveBlendAdjuster((GPUImageDissolveBlendFilter) filter);
            if (filter instanceof GPUImageGaussianBlurFilter) return new GaussianBlurAdjuster((GPUImageGaussianBlurFilter) filter);
            if (filter instanceof GPUImageCrosshatchFilter) return new CrosshatchBlurAdjuster((GPUImageCrosshatchFilter) filter);
            if (filter instanceof GPUImageBulgeDistortionFilter) return new BulgeDistortionAdjuster((GPUImageBulgeDistortionFilter) filter);
            if (filter instanceof GPUImageGlassSphereFilter) return new GlassSphereAdjuster((GPUImageGlassSphereFilter) filter);
            if (filter instanceof GPUImageHazeFilter) return new HazeAdjuster((GPUImageHazeFilter) filter);
            if (filter instanceof GPUImageSphereRefractionFilter) return new SphereRefractionAdjuster((GPUImageSphereRefractionFilter) filter);
            if (filter instanceof GPUImageSwirlFilter) return new SwirlAdjuster((GPUImageSwirlFilter) filter);
            if (filter instanceof GPUImageColorBalanceFilter) return new ColorBalanceAdjuster((GPUImageColorBalanceFilter) filter);
            if (filter instanceof GPUImageLevelsFilter) return new LevelsMinMidAdjuster((GPUImageLevelsFilter) filter);
            if (filter instanceof GPUImageBilateralBlurFilter) return new BilateralAdjuster((GPUImageBilateralBlurFilter) filter);
            if (filter instanceof GPUImageTransformFilter) return new RotateAdjuster((GPUImageTransformFilter) filter);
            if (filter instanceof GPUImageSolarizeFilter) return new SolarizeAdjuster((GPUImageSolarizeFilter) filter);
            if (filter instanceof GPUImageVibranceFilter) return new VibranceAdjuster((GPUImageVibranceFilter) filter);
            return null;
        }

        /* —— 下面所有 Adjuster 均为静态内部类 —— */
        private static class SharpnessAdjuster extends Adjuster<GPUImageSharpenFilter> {
            SharpnessAdjuster(GPUImageSharpenFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setSharpness(range(percentage, -4f, 4f));
            }
        }
        private static class SepiaAdjuster extends Adjuster<GPUImageSepiaToneFilter> {
            SepiaAdjuster(GPUImageSepiaToneFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setIntensity(range(percentage, 0f, 2f));
            }
        }
        private static class ContrastAdjuster extends Adjuster<GPUImageContrastFilter> {
            ContrastAdjuster(GPUImageContrastFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setContrast(range(percentage, 0f, 2f));
            }
        }
        private static class GammaAdjuster extends Adjuster<GPUImageGammaFilter> {
            GammaAdjuster(GPUImageGammaFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setGamma(range(percentage, 0f, 3f));
            }
        }
        private static class BrightnessAdjuster extends Adjuster<GPUImageBrightnessFilter> {
            BrightnessAdjuster(GPUImageBrightnessFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setBrightness(range(percentage, -1f, 1f));
            }
        }
        private static class SobelAdjuster extends Adjuster<GPUImageSobelEdgeDetectionFilter> {
            SobelAdjuster(GPUImageSobelEdgeDetectionFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setLineSize(range(percentage, 0f, 5f));
            }
        }
        private static class ThresholdAdjuster extends Adjuster<GPUImageThresholdEdgeDetectionFilter> {
            ThresholdAdjuster(GPUImageThresholdEdgeDetectionFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setLineSize(range(percentage, 0f, 5f));
                filter.setThreshold(0.9f);
            }
        }
        private static class ThreeXThreeConvolutionAdjuster extends Adjuster<GPUImage3x3ConvolutionFilter> {
            ThreeXThreeConvolutionAdjuster(GPUImage3x3ConvolutionFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setConvolutionKernel(new float[]{-1f, 0f, 1f, -2f, 0f, 2f, -1f, 0f, 1f});
            }
        }
        private static class EmbossAdjuster extends Adjuster<GPUImageEmbossFilter> {
            EmbossAdjuster(GPUImageEmbossFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setIntensity(range(percentage, 0f, 4f));
            }
        }
        private static class PosterizeAdjuster extends Adjuster<GPUImagePosterizeFilter> {
            PosterizeAdjuster(GPUImagePosterizeFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setColorLevels(range(percentage, 1, 50));
            }
        }
        private static class HueAdjuster extends Adjuster<GPUImageHueFilter> {
            HueAdjuster(GPUImageHueFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setHue(range(percentage, 0f, 360f));
            }
        }
        private static class PixelationAdjuster extends Adjuster<GPUImagePixelationFilter> {
            PixelationAdjuster(GPUImagePixelationFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setPixel(range(percentage, 1f, 100f));
            }
        }
        private static class SaturationAdjuster extends Adjuster<GPUImageSaturationFilter> {
            SaturationAdjuster(GPUImageSaturationFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setSaturation(range(percentage, 0f, 2f));
            }
        }
        private static class ExposureAdjuster extends Adjuster<GPUImageExposureFilter> {
            ExposureAdjuster(GPUImageExposureFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setExposure(range(percentage, -10f, 10f));
            }
        }
        private static class HighlightShadowAdjuster extends Adjuster<GPUImageHighlightShadowFilter> {
            HighlightShadowAdjuster(GPUImageHighlightShadowFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setShadows(range(percentage, 0f, 1f));
                filter.setHighlights(range(percentage, 0f, 1f));
            }
        }
        private static class MonochromeAdjuster extends Adjuster<GPUImageMonochromeFilter> {
            MonochromeAdjuster(GPUImageMonochromeFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setIntensity(range(percentage, 0f, 1f));
            }
        }
        private static class OpacityAdjuster extends Adjuster<GPUImageOpacityFilter> {
            OpacityAdjuster(GPUImageOpacityFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setOpacity(range(percentage, 0f, 1f));
            }
        }
        private static class RGBAdjuster extends Adjuster<GPUImageRGBFilter> {
            RGBAdjuster(GPUImageRGBFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setRed(range(percentage, 0f, 1f));
            }
        }
        private static class WhiteBalanceAdjuster extends Adjuster<GPUImageWhiteBalanceFilter> {
            WhiteBalanceAdjuster(GPUImageWhiteBalanceFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setTemperature(range(percentage, 2000f, 8000f));
            }
        }
        private static class VignetteAdjuster extends Adjuster<GPUImageVignetteFilter> {
            VignetteAdjuster(GPUImageVignetteFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setVignetteStart(range(percentage, 0f, 1f));
            }
        }
        private static class LuminanceThresholdAdjuster extends Adjuster<GPUImageLuminanceThresholdFilter> {
            LuminanceThresholdAdjuster(GPUImageLuminanceThresholdFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setThreshold(range(percentage, 0f, 1f));
            }
        }
        private static class DissolveBlendAdjuster extends Adjuster<GPUImageDissolveBlendFilter> {
            DissolveBlendAdjuster(GPUImageDissolveBlendFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setMix(range(percentage, 0f, 1f));
            }
        }
        private static class GaussianBlurAdjuster extends Adjuster<GPUImageGaussianBlurFilter> {
            GaussianBlurAdjuster(GPUImageGaussianBlurFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setBlurSize(range(percentage, 0f, 1f));
            }
        }
        private static class CrosshatchBlurAdjuster extends Adjuster<GPUImageCrosshatchFilter> {
            CrosshatchBlurAdjuster(GPUImageCrosshatchFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setCrossHatchSpacing(range(percentage, 0f, 0.06f));
                filter.setLineWidth(range(percentage, 0f, 0.006f));
            }
        }
        private static class BulgeDistortionAdjuster extends Adjuster<GPUImageBulgeDistortionFilter> {
            BulgeDistortionAdjuster(GPUImageBulgeDistortionFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setRadius(range(percentage, 0f, 1f));
                filter.setScale(range(percentage, -1f, 1f));
            }
        }
        private static class GlassSphereAdjuster extends Adjuster<GPUImageGlassSphereFilter> {
            GlassSphereAdjuster(GPUImageGlassSphereFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setRadius(range(percentage, 0f, 1f));
            }
        }
        private static class HazeAdjuster extends Adjuster<GPUImageHazeFilter> {
            HazeAdjuster(GPUImageHazeFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setDistance(range(percentage, -0.3f, 0.3f));
                filter.setSlope(range(percentage, -0.3f, 0.3f));
            }
        }
        private static class SphereRefractionAdjuster extends Adjuster<GPUImageSphereRefractionFilter> {
            SphereRefractionAdjuster(GPUImageSphereRefractionFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setRadius(range(percentage, 0f, 1f));
            }
        }
        private static class SwirlAdjuster extends Adjuster<GPUImageSwirlFilter> {
            SwirlAdjuster(GPUImageSwirlFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setAngle(range(percentage, 0f, 2f));
            }
        }
        private static class ColorBalanceAdjuster extends Adjuster<GPUImageColorBalanceFilter> {
            ColorBalanceAdjuster(GPUImageColorBalanceFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setMidtones(new float[]{
                        range(percentage, 0f, 1f),
                        range(percentage / 2, 0f, 1f),
                        range(percentage / 3, 0f, 1f)
                });
            }
        }
        private static class LevelsMinMidAdjuster extends Adjuster<GPUImageLevelsFilter> {
            LevelsMinMidAdjuster(GPUImageLevelsFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setMin(0f, range(percentage, 0f, 1f), 1f);
            }
        }
        private static class BilateralAdjuster extends Adjuster<GPUImageBilateralBlurFilter> {
            BilateralAdjuster(GPUImageBilateralBlurFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setDistanceNormalizationFactor(range(percentage, 0f, 15f));
            }
        }
        private static class RotateAdjuster extends Adjuster<GPUImageTransformFilter> {
            RotateAdjuster(GPUImageTransformFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                float[] transform = new float[16];
                Matrix.setRotateM(transform, 0, 360f * percentage / 100f, 0f, 0f, 1f);
                filter.setTransform3D(transform);
            }
        }
        private static class SolarizeAdjuster extends Adjuster<GPUImageSolarizeFilter> {
            SolarizeAdjuster(GPUImageSolarizeFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setThreshold(range(percentage, 0f, 1f));
            }
        }
        private static class VibranceAdjuster extends Adjuster<GPUImageVibranceFilter> {
            VibranceAdjuster(GPUImageVibranceFilter filter) { super(filter); }
            @Override public void adjust(int percentage) {
                filter.setVibrance(range(percentage, -1.2f, 1.2f));
            }
        }
    }
}